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
  },
  sendLogin(username, password) {
    const creds = "Basic " + btoa(username + ":" + password);
    const config = {
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
      'authorization': creds
      }
    };
    return axios.get('api/accounts', config)
                .then(function (response) {
                  return response.data;
                })
                .catch(function (error) {
                  throw error.response;
                });
  },
  signup(username, password) {
    const command = {username: username, password: password, roles: [{"id":1,"name":"USER"}]};
    const config = {
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      }
    }
    return axios.post('api/accounts/signup', command, config)
         .then(function (response) {
           return response.data;
         })
         .catch(function (error) {
           throw error.response;
         });
  }
}
