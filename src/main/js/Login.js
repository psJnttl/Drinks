import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Col, FormGroup, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import App from './App';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';
import SimpleConfirmationModal from './components/SimpleConfirmationModal';

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {authenticated: false, account: {}, loginFail: false,
                  username: "", password: "",signupFail: false,
                  notification2:"", signupSuccess: false}
    this.setAccountData = this.setAccountData.bind(this);
    this.sendLogin = this.sendLogin.bind(this);
    this.sendSignup = this.sendSignup.bind(this);
    this.closeLoginFailModal = this.closeLoginFailModal.bind(this);
    this.closeSignupFailModal = this.closeSignupFailModal.bind(this);
    this.closeSignupSuccessModal = this.closeSignupSuccessModal.bind(this);
    this.handleAuthenticationState = this.handleAuthenticationState
  }

  setAccountData(data) {
    // copy contents, not reference
    console.log(data);
  }

  sendLogin(username, password) {
    console.log("username/password: " + username + " / " + password);
    this.setState({username: username});
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
              self.setState({authenticated: false, loginFail: true});
            }
        });
  }
  closeLoginFailModal() {
    this.setState({loginFail: false});
  }
  closeSignupFailModal() {
    this.setState({signupFail: false});
  }
  closeSignupSuccessModal() {
    this.setState({signupSuccess: false});
    this.sendLogin(this.state.username, this.state.password);
  }
  sendSignup(username, password) {
    this.setState({username: username, password: password});
    const command = {username: username, password: password, roles: [{"id":1,"name":"USER"}]};
    console.log("sendSignup, command: ");
    console.log(command);
    const config = {
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      }
    }
    const self = this;
    axios.post('api/account/signup', command, config)
         .then(function (response) {
           self.setState({authenticated: false, signupFail: false,
             notification2: "Click OK to proceed to site.", signupSuccess: true});

         })
        .catch(function (response) {
            if (409 == response.response.status) {
              debugger;
              self.setState({authenticated: false, signupFail: true, notification2: "Username already taken."});
            }
        });

  }
  componentDidMount() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/account', config)
         .then(function (response) {
              self.setAccountData(response.data);
              self.setState({authenticated: true});
         })
        .catch(function (response) {
            if (401 == response.response.status) {
              self.setState({authenticated: false});
            }
        });
  }
  handleAuthenticationState(state) {
    const value = state;
    this.setState({authenticated: value});
  }

  render() {
    const element =
    <div>
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
                <SignupForm signupFn={this.sendSignup}/>
              </Tab.Pane>
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
      <SimpleConfirmationModal
        modalOpen={this.state.loginFail}
        title="LOGIN FAILED!"
        notification = "Failed to log in user "
        name={this.state.username}
        reply={this.closeLoginFailModal} />
      <SimpleConfirmationModal
        modalOpen={this.state.signupFail}
        title="SIGNUP FAILED!"
        notification = "Failed to create account with username "
        notification2 = {this.state.notification2}
        name={this.state.username}
        reply={this.closeSignupFailModal} />
      <SimpleConfirmationModal
        modalOpen={this.state.signupSuccess}
        header="successModalHeader"
        title="SIGNUP SUCCESS!"
        notification = "Successfully created an account with username "
        notification2 = {this.state.notification2}
        name={this.state.username}
        reply={this.closeSignupSuccessModal} />
    </div>;

    return (
      this.state.authenticated ?
        <App
          authenticated={this.state.authenticated}
          changeAuthState={this.handleAuthenticationState}
        /> :
        element
    );
  }
}
Login.PropTypes = {}
Login.defaultProps = {}
export default Login;
