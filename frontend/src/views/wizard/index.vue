<template>
  <div
    v-if="loading"
    v-loading="loading"
    style="position:absolute;top:55px;width: 100%;height: calc(100% - 55px);"
  />
  <div v-else-if="homeLink">
    <iframe id="mobsf" :src="homeLink" frameborder="0" style="position:absolute;top:55px;width: 100%;height: calc(100% - 55px);" />
  </div>
  <el-row v-else class="main_container">
    <el-row class="head">
      <span class="hint_head">{{ $t('wizard.welcome_title') }}</span> <br>
      <span class="hint_content">{{ $t('wizard.welcome_hint') }}</span>
    </el-row>
    <el-row class="card_container">
      <info-card v-for="(cardDetail,index) in cardList" :key="index">
        <component :is="cardDetail.component" :img-index="index" :details="cardDetail" />
      </info-card>
    </el-row>
  </el-row>
</template>

<script>

import Card from '@/views/wizard/card'
import DemoVideo from '@/views/wizard/details/DemoVideo'
import OnlineDocument from '@/views/wizard/details/OnlineDocument'
import LatestDevelopments from '@/views/wizard/details/LatestDevelopments'
import TeachingVideo from '@/views/wizard/details/TeachingVideo'
import EnterpriseEdition from '@/views/wizard/details/EnterpriseEdition'
import ContactUs from '@/views/wizard/details/ContactUs'
import InfoCard from '@/views/wizard/infoCard'
import CardDetail from '@/views/wizard/details/CardDetail'
import { blogLastActive } from '@/api/wizard/wizard'

export default {
  name: 'Wizard',
  components: { InfoCard, Card, DemoVideo, OnlineDocument, LatestDevelopments, TeachingVideo, EnterpriseEdition, ContactUs, CardDetail },
  data() {
    return {
      cardList: [
        {
          head: this.$t('wizard.contact_us'),
          content: this.$t('wizard.email') + 'xiaqiankun@outlook.com<br>' + this.$t('wizard.tel') + '<br>' + this.$t('wizard.web') + '<a target="_blank" href="https://www.icl.site">www.icl.site</a>',
          bottom: '',
          href: 'https://www.icl.site',
          component: 'CardDetail'
        }
      ],
      loading: true

    }
  },
  computed: {
    homeLink() {
      if (this.$store.getters.uiInfo && this.$store.getters.uiInfo['ui.homeLink'] && this.$store.getters.uiInfo['ui.homeLink'].paramValue) {
        return this.$store.getters.uiInfo['ui.homeLink'].paramValue
      }
      return null
    }
  },
  mounted() {
    setTimeout(() => {
      this.loading = false
    }, 1000)
  },

  created() {
    this.init()
  },
  methods: {
    init() {
      blogLastActive().then(res => {
        const blogsInfo = res.data[0]
        this.cardList[2].content = blogsInfo.title
        this.cardList[2].bottom = blogsInfo.time
      })
    }
  }
}

</script>

<style lang="scss" scoped>
  .main_container {
  }
  .head {
    text-align: center;
    color: white;
    padding: 10px;
    margin-top: 35px;
    background-size: 100% 100% !important;
    background-image: url('../../assets/banner.png');
  }
  .hint_head {
    line-height: 50px;
    font-weight: bold;
    font-size: 25px;
  }
  .hint_content {
    line-height: 50px;
    font-size: 15px;
  }

  .card_container {
   vertical-align: middle;

  }
</style>
