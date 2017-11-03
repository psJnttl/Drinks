import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, FormGroup, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import App from './App';

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {authenticated: false, account: {}}
    this.method = this.method.bind(this);
  }
  method() {}
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
                <LoginForm />
              </Tab.Pane>
              <Tab.Pane eventKey="signupTab">
                Sign up here
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

class LoginForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {name: "", password: "",}
    this.onChangeName = this.onChangeName.bind(this);
    this.onChangePassword = this.onChangePassword.bind(this);
    this.submit = this.submit.bind(this);
  }
  onChangeName(e) {
    const value = e.target.value;
    this.setState({name: value});
  }
  onChangePassword(e) {
    const value = e.target.value;
    this.setState({password: value});
  }
  submit() {
    console.log("login/password: " + this.state.name + "/" + this.state.password);
  }
  render() {
    return (
      <div>
        <h3>Drink archive</h3>
        <form>
          <FormGroup controlId="formHorizontalInput">
            <input
              className="modalinput"
              type="text"
              placeholder="username"
              onChange={this.onChangeName}
              value={this.state.name}
            autoComplete="off" />
          </FormGroup>
          <FormGroup controlId="formHorizontalInput">
            <input
              className="modalinput"
              type="password"
              placeholder="password"
              onChange={this.onChangePassword}
              value={this.state.password}
            autoComplete="off" />
          </FormGroup>
          <Button bsStyle="success" disabled={!this.state.name || !this.state.password} onClick={ () => this.submit() }>Login</Button>
        </form>
      </div>
    );
  }
}
LoginForm.PropTypes = {}
LoginForm.defaultProps = {}
