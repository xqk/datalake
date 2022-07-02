package io.datalake.service.sys;

import io.datalake.auth.api.dto.CurrentUserDto;
import io.datalake.auth.service.ExtAuthService;
import io.datalake.commons.constants.AuthConstants;
import io.datalake.commons.utils.AuthUtils;
import io.datalake.commons.utils.BeanUtils;
import io.datalake.commons.utils.CodingUtil;
import io.datalake.controller.sys.base.BaseGridRequest;
import io.datalake.controller.sys.request.LdapAddRequest;
import io.datalake.controller.sys.request.SysUserCreateRequest;
import io.datalake.controller.sys.request.SysUserPwdRequest;
import io.datalake.controller.sys.request.SysUserStateRequest;
import io.datalake.controller.sys.response.SysUserGridResponse;
import io.datalake.controller.sys.response.SysUserRole;
import io.datalake.ext.ExtSysUserMapper;
import io.datalake.ext.query.GridExample;
import io.datalake.i18n.Translator;
import io.datalake.plugins.common.base.domain.SysUser;
import io.datalake.plugins.common.base.domain.SysUserExample;
import io.datalake.plugins.common.base.domain.SysUsersRolesExample;
import io.datalake.plugins.common.base.domain.SysUsersRolesKey;
import io.datalake.plugins.common.base.mapper.SysUserMapper;
import io.datalake.plugins.common.base.mapper.SysUsersRolesMapper;
import io.datalake.plugins.common.entity.XpackLdapUserEntity;
import io.datalake.plugins.xpack.oidc.dto.SSOUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserService {


    @Value("${datalake.init_password:DataLake123..}")
    private String DEFAULT_PWD;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUsersRolesMapper sysUsersRolesMapper;

    @Resource
    private ExtSysUserMapper extSysUserMapper;

    @Autowired
    private ExtAuthService extAuthService;


    public List<SysUserGridResponse> query(BaseGridRequest request) {

        GridExample gridExample = request.convertExample();
        List<SysUserGridResponse> lists = extSysUserMapper.query(gridExample);
        lists.forEach(item -> {

            List<SysUserRole> roles = item.getRoles();
            List<Long> roleIds = roles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            item.setRoleIds(roleIds);
        });
        return lists;
    }

    @Transactional
    public int save(SysUserCreateRequest request) {
        checkUsername(request);
        checkEmail(request);
        checkNickName(request);
        SysUser user = BeanUtils.copyBean(new SysUser(), request);
        long now = System.currentTimeMillis();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsAdmin(false);
        user.setFrom(0);
        if (ObjectUtils.isEmpty(user.getPassword()) || StringUtils.equals(user.getPassword(), DEFAULT_PWD)) {
            user.setPassword(CodingUtil.md5(DEFAULT_PWD));
        } else {
            user.setPassword(CodingUtil.md5(user.getPassword()));
        }
        if (StringUtils.isEmpty(user.getLanguage())) {
            user.setLanguage("zh_CN");
        }
        int insert = sysUserMapper.insert(user);
        SysUser dbUser = findOne(user);
        request.setUserId(dbUser.getUserId());
        saveUserRoles(dbUser.getUserId(), request.getRoleIds());//插入用户角色关联
        return insert;
    }

    @Transactional
    public void saveOIDCUser(SSOUserInfo ssoUserInfo) {
        long now = System.currentTimeMillis();
        SysUser sysUser = new SysUser();
        sysUser.setUsername(ssoUserInfo.getUsername());
        sysUser.setNickName(ssoUserInfo.getNickName());
        sysUser.setEmail(ssoUserInfo.getEmail());
        sysUser.setPassword(CodingUtil.md5(DEFAULT_PWD));
        sysUser.setCreateTime(now);
        sysUser.setUpdateTime(now);
        sysUser.setEnabled(1L);
        sysUser.setLanguage("zh_CN");
        sysUser.setFrom(2);
        sysUser.setIsAdmin(false);
        sysUser.setSub(ssoUserInfo.getSub());
        sysUserMapper.insert(sysUser);
        SysUser dbUser = findOne(sysUser);
        if (null != dbUser && null != dbUser.getUserId()) {
            // oidc默认角色是普通员工
            List<Long> roleIds = new ArrayList<Long>();
            roleIds.add(2L);
            saveUserRoles( dbUser.getUserId(), roleIds);
        }
    }

    public String defaultPWD() {
        return DEFAULT_PWD;
    }

    @Transactional
    public void saveLdapUsers(LdapAddRequest request) {
        long now = System.currentTimeMillis();

        List<XpackLdapUserEntity> users = request.getUsers();
        List<SysUser> sysUsers = users.stream().map(user -> {
            SysUser sysUser = BeanUtils.copyBean(new SysUser(), user);
            sysUser.setUsername(user.getUsername());
            sysUser.setNickName(user.getNickname());
            sysUser.setDeptId(request.getDeptId());
            sysUser.setPassword(CodingUtil.md5(DEFAULT_PWD));
            sysUser.setCreateTime(now);
            sysUser.setUpdateTime(now);
            sysUser.setEnabled(request.getEnabled());
            sysUser.setLanguage("zh_CN");
            sysUser.setIsAdmin(false);
            sysUser.setFrom(1);
            return sysUser;
        }).collect(Collectors.toList());

        sysUsers.forEach(sysUser -> {
            sysUserMapper.insert(sysUser);
            SysUser dbUser = findOne(sysUser);
            if (null != dbUser && null != dbUser.getUserId()) {
                saveUserRoles( dbUser.getUserId(), request.getRoleIds());
            }
        });
    }

    public boolean validateLoginType(Integer from, Integer loginType) {

        return ObjectUtils.isNotEmpty(from) && ObjectUtils.isNotEmpty(loginType) && from == loginType;
    }

    public List<String> ldapUserNames() {

        List<String> usernames = extSysUserMapper.ldapUserNames(1);
        return usernames;

    }

    /**
     * 修改用户密码清楚缓存
     *
     * @param request
     * @return
     */
    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #request.userId")
    @Transactional
    public int update(SysUserCreateRequest request) {
        checkUsername(request);
        checkEmail(request);
        checkNickName(request);
        if (StringUtils.isEmpty(request.getPassword())) {
            request.setPassword(null);
        }
        SysUser user = BeanUtils.copyBean(new SysUser(), request);
        long now = System.currentTimeMillis();
        user.setUpdateTime(now);
        deleteUserRoles(user.getUserId());//先删除用户角色关联
        saveUserRoles(user.getUserId(), request.getRoleIds());//再插入角色关联
        if (ObjectUtils.isEmpty(user.getDeptId())) user.setDeptId(0L);
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 用户修改个人信息
     *
     * @param request
     * @return
     */
    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #request.userId")
    @Transactional
    public int updatePersonInfo(SysUserCreateRequest request) {
        SysUser user = BeanUtils.copyBean(new SysUser(), request);
        long now = System.currentTimeMillis();
        user.setUpdateTime(now);
        return sysUserMapper.updateByPrimaryKeySelective(user);

    }

    /**
     * 更新用户基本信息
     * 只允许修改 email, nickname, phone
     * 防止此接口被恶意利用更改不允许更改的信息，新建SysUser对象并只设置部分值
     * @param request
     * @return
     */
    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #request.userId")
    @Transactional
    public int updatePersonBasicInfo(SysUserCreateRequest request) {
        checkEmail(request);
        checkNickName(request);
        SysUser user = new SysUser();
        long now = System.currentTimeMillis();
        user.setUserId(request.getUserId());
        user.setUpdateTime(now);
        user.setEmail(request.getEmail());
        user.setNickName(request.getNickName());
        user.setPhone(request.getPhone());
        return sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #request.userId")
    public int updateStatus(SysUserStateRequest request) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(request.getUserId());
        sysUser.setEnabled(request.getEnabled());
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * 修改用户密码清除缓存
     *
     * @param request
     * @return
     */
    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #request.userId")
    public int updatePwd(SysUserPwdRequest request) {
        CurrentUserDto user = AuthUtils.getUser();

        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        }
        if (!StringUtils.equals(CodingUtil.md5(request.getPassword()), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(user.getUserId());
        if (!request.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,30}$")) {
            throw new RuntimeException("密码格式错误");
        }
        sysUser.setPassword(CodingUtil.md5(request.getNewPassword()));
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #request.userId")
    public int adminUpdatePwd(SysUserPwdRequest request) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(request.getUserId());
        sysUser.setPassword(CodingUtil.md5(request.getNewPassword()));
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }


    /**
     * 删除用户角色关联
     *
     * @param userId
     * @return
     */
    private int deleteUserRoles(Long userId) {
        SysUsersRolesExample example = new SysUsersRolesExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return sysUsersRolesMapper.deleteByExample(example);
    }

    /**
     * 保存用户角色关联
     *
     * @param userId
     * @param roleIds
     */
    private void saveUserRoles(Long userId, List<Long> roleIds) {
        roleIds.forEach(roleId -> {
            SysUsersRolesKey sysUsersRolesKey = new SysUsersRolesKey();
            sysUsersRolesKey.setUserId(userId);
            sysUsersRolesKey.setRoleId(roleId);
            sysUsersRolesMapper.insert(sysUsersRolesKey);
        });
    }

    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #userId")
    @Transactional
    public int delete(Long userId) {
        extAuthService.clearUserResource(userId);
        deleteUserRoles(userId);
        return sysUserMapper.deleteByPrimaryKey(userId);
    }

    public SysUser findOne(SysUser user) {
        if (ObjectUtils.isEmpty(user)) return null;
        if (ObjectUtils.isNotEmpty(user.getUserId())) {
            return sysUserMapper.selectByPrimaryKey(user.getUserId());
        }
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria = example.createCriteria();
        if (ObjectUtils.isNotEmpty(user.getUsername())) {
            criteria.andUsernameEqualTo(user.getUsername());
            List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(sysUsers)) return sysUsers.get(0);
        }
        return null;
    }

    public void validateExistUser(String userName, String nickName, String email) {
        SysUserExample example = new SysUserExample();
        if (StringUtils.isNotBlank(userName)) {
            example.createCriteria().andUsernameEqualTo(userName);
            List<SysUser> users = sysUserMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(users)) {
                throw new RuntimeException("用户ID【"+userName+"】已存在,请联系管理员");
            }
        }

        if (StringUtils.isNotBlank(nickName)) {
            example.createCriteria().andNickNameEqualTo(nickName);
            List<SysUser> users = sysUserMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(users)) {
                throw new RuntimeException("用户姓名【"+nickName+"】已存在,请联系管理员");
            }
        }
        example.clear();
        if (StringUtils.isNotBlank(email)) {
            example.createCriteria().andEmailEqualTo(email);
            List<SysUser> users = sysUserMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(users)) {
                throw new RuntimeException("用户邮箱【"+email+"】已存在,请联系管理员");
            }
        }
    }


    public List<SysUser> users(List<Long> userIds) {
        return userIds.stream().map(sysUserMapper::selectByPrimaryKey).collect(Collectors.toList());
    }

    @CacheEvict(value = AuthConstants.USER_CACHE_NAME, key = "'user' + #userId")
    public void setLanguage(Long userId, String language) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLanguage(language);
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    private void checkUsername(SysUserCreateRequest request) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        if (request.getUserId() != null) {
            criteria.andUserIdNotEqualTo(request.getUserId());
        }
        criteria.andUsernameEqualTo(request.getUsername());
        List<SysUser> sysUsers = sysUserMapper.selectByExample(sysUserExample);
        if (CollectionUtils.isNotEmpty(sysUsers)) {
            throw new RuntimeException(Translator.get("i18n_username_exists"));
        }
    }

    private void checkEmail(SysUserCreateRequest request) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        if (request.getUserId() != null) {
            criteria.andUserIdNotEqualTo(request.getUserId());
        }
        criteria.andEmailEqualTo(request.getEmail());
        List<SysUser> sysUsers = sysUserMapper.selectByExample(sysUserExample);
        if (CollectionUtils.isNotEmpty(sysUsers)) {
            throw new RuntimeException(Translator.get("i18n_email_exists"));
        }
    }

    private void checkNickName(SysUserCreateRequest request) {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        if (request.getUserId() != null) {
            criteria.andUserIdNotEqualTo(request.getUserId());
        }
        criteria.andNickNameEqualTo(request.getNickName());
        List<SysUser> sysUsers = sysUserMapper.selectByExample(sysUserExample);
        if (CollectionUtils.isNotEmpty(sysUsers)) {
            throw new RuntimeException(Translator.get("i18n_nickname_exists"));
        }
    }


}
