import axios from 'axios'
import { Loading } from 'element-ui'

axios.defaults.withCredentials = true
// axios.defaults.baseURL = 'http://localhost:8082'
var loadinginstace
// 创建一个axios实例
const service = axios.create({
  // 请求超时时间
  timeout: 15000
})

// 添加请求拦截器
service.interceptors.request.use(
  config => {
    loadinginstace = Loading.service({ fullscreen: true })
    return config
  },
  err => {
    loadinginstace.close
    alert('请求超时', err)
  }
)

service.interceptors.response.use(
  response => {
    loadinginstace.close()
    let res = {}
    res.status = response.status
    res.data = response.data
    return res
  },
  err => {
    loadinginstace.close()
    alert('请求超时', err)
  }
)

export default service
