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
                  throw error;
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
                   throw error;
                });
  },
}
