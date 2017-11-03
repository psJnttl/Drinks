import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, FormGroup, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import App from './App';
import LoginForm from './components/LoginForm';
import SignupForm from './components/SignupForm';

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
