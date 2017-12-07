import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, FormGroup, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import App from './App';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';
import SimpleInformationModal from './components/SimpleInformationModal';
import _ from 'lodash';
import NetworkApi from './components/NetworkApi';

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {authState: {id: 0, authenticated: false, username:"", admin: false},  loginFail: false,
                  username: "", password: "",signupFail: false,
                  notification2:"", signupSuccess: false}
    this.setAccountData = this.setAccountData.bind(this);
    this.eraseAuthData = this.eraseAuthData.bind(this);
    this.sendLogin = this.sendLogin.bind(this);
    this.sendSignup = this.sendSignup.bind(this);
    this.closeLoginFailModal = this.closeLoginFailModal.bind(this);
    this.closeSignupFailModal = this.closeSignupFailModal.bind(this);
    this.closeSignupSuccessModal = this.closeSignupSuccessModal.bind(this);
    this.handleAuthenticationState = this.handleAuthenticationState.bind(this);
    this.loginViaGithub = this.loginViaGithub.bind(this);
  }

  setAccountData(data) {
    const index = _.findIndex(data.roles, function(u) { return u.name === "ADMIN" });
    const newData = _.assign({}, {username: data.username, authenticated: true, id: data.id });
    let auth
    if (-1 === index) {
      auth = _.assign(newData, {admin: false});
    }
    else {
      auth = _.assign(newData, {admin: true});
    }
    this.setState({authState: auth});
  }

  eraseAuthData() {
    this.setState({authState: {authenticated: false, username:"", admin: false}});
  }

  sendLogin(username, password) {
    this.setState({username: username});
    const self = this;
    NetworkApi.sendLogin(username, password)
         .then(function (response) {
              self.setAccountData(response);
         })
        .catch(function (response) {
            if (401 === response.status) {
              self.eraseAuthData();
              self.setState({loginFail: true});
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
    const self = this;
    NetworkApi.signup(username, password)
         .then(function (response) {
           self.setState({signupFail: false,
             notification2: "Click OK to proceed to site.", signupSuccess: true});
         })
        .catch(function (response) {
            if (409 === response.status) {
              self.setState({signupFail: true, notification2: "Username already taken."});
            }
        });

  }

  loginViaGithub() {
    const self = this;
    NetworkApi.get("/api/login/github")
        .then(function (response) {
          console.log(" OAUTH2 OK!");
        })
        .catch(function (response) {
          console.log(" OAUTH2 FAILURE!");
        });
  }

  componentDidMount() {
    const self = this;
    NetworkApi.get('api/accounts')
         .then(function (response) {
              self.setAccountData(response);
         })
        .catch(function (response) {
            if (401 === response.status) {
              self.eraseAuthData()
            }
        });
  }

  handleAuthenticationState(state) {
    const value = state;
    if (false === state) {
      this.eraseAuthData();
    }
    else {
      const auth = _.assign(this.state.authState, {authenticated: true});
      this.setState({authState: auth});
    }
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
      <SimpleInformationModal
        modalOpen={this.state.loginFail}
        title="LOGIN FAILED!"
        notification = "Failed to log in user "
        name={this.state.username}
        reply={this.closeLoginFailModal} />
      <SimpleInformationModal
        modalOpen={this.state.signupFail}
        title="SIGNUP FAILED!"
        notification = "Failed to create account with username "
        notification2 = {this.state.notification2}
        name={this.state.username}
        reply={this.closeSignupFailModal} />
      <SimpleInformationModal
        modalOpen={this.state.signupSuccess}
        header="successModalHeader"
        title="SIGNUP SUCCESS!"
        notification = "Successfully created an account with username "
        notification2 = {this.state.notification2}
        name={this.state.username}
        reply={this.closeSignupSuccessModal} />
      <Button bsStyle="primary" onClick={ () => this.loginViaGithub() }>github</Button>
    </div>;
    const state = this.state;
    const aState = this.state.authState.authenticated;
    return (
      this.state.authState.authenticated ?
        <App
          authState={this.state.authState}
          changeAuthState={this.handleAuthenticationState}
        /> :
        element
    );
  }
}
Login.PropTypes = {}
Login.defaultProps = {}
export default Login;
