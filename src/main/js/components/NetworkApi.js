import axios from 'axios';
import _ from 'lodash';

export default  {

  get(url) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    return axios.get(url, config)
                .then(function (response) {
                  return response.data;
                })
                .catch(function (error) {
                  throw error.response;
                });
  },
  post(url, command) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const cmd  =  _.assign({}, command);
    return axios.post(url, cmd, config)
                .then(function (response) {
                   return response.data;
                })
                .catch(function (error) {
                   throw error.response;
                });
  },
  delete(url1, item) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const url = url1 + '/' + item.id;
    return axios.delete(url, config)
                .then(function (response) {
                  return response.data;
                })
                .catch(function (error) {
                  throw error.response;
                });
  },
  put(url1, command) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const cmd = _.assign({}, command);
    const url = url1 + '/' + cmd.id;
    return axios.put(url, cmd, config)
                .then(function (response) {
                  return response.data;
                })
                .catch(function (error) {
                  throw error.response;
                });
  }
}
