import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Col, FormGroup, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import App from './App';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {authenticated: false, account: {}}
    this.setAccountData = this.setAccountData.bind(this);
    this.sendLogin = this.sendLogin.bind(this);
  }

  setAccountData(data) {
    // copy contents, not reference
    console.log(data);
  }

  sendLogin(username, password) {
    const creds = "Basic " + btoa(username + ":" + password);
    const config = {
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
      'authorization': creds
      }
    };
    const self = this;
    axios.get('api/account', config)
         .then(function (response) {
              self.setAccountData(response.data); // console.log(response);
              self.setState({authenticated: true});
         })
        .catch(function (response) {
            if (401 == response.response.status) {
              self.setState({authenticated: false});
            }
        });
  }

  componentDidMount() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/account', config)
         .then(function (response) {
              self.setAccountData(response.data);
              this.setState({authenticated: true});
         })
        .catch(function (response) {
            if (401 == response.response.status) {
              this.setState({authenticated: false});
            }
        });
  }

  render() {
    const element =
      <Tab.Container id="tabs-with-dropdown" defaultActiveKey="loginTab">
        <Row className="clearfix">
          <Col sm={6}>
            <Nav bsStyle="tabs">
              <NavItem eventKey="loginTab">
                Login
              </NavItem>
              <NavItem eventKey="signupTab">
                Sign up
              </NavItem>
            </Nav>
          </Col>
          <Col sm={12}>
            <Tab.Content animation>
              <Tab.Pane eventKey="loginTab">
                <LoginForm loginFn={this.sendLogin}/>
              </Tab.Pane>
              <Tab.Pane eventKey="signupTab">
                <SignupForm />
              </Tab.Pane>
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>;

    // alkaa kysymyksellä käyttäjälle Buttonit Login, Signup
    // oikeasti serveriltä pitää kysyä mitä näytetään
    return (
      this.state.authenticated ? <App /> : element
    );
  }
}
Login.PropTypes = {}
Login.defaultProps = {}
export default Login;
