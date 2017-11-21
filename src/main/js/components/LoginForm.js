import React from 'react';
import PropTypes from 'prop-types';
import {Button, FormGroup} from 'react-bootstrap';

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
    this.props.loginFn(this.state.name, this.state.password);
  }
  render() {
    return (
      <div style={ {margin: 20} }>
        <h3>Drinks archive</h3>
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
LoginForm.PropTypes = {
  loginFn: PropTypes.func.isRequired,
}
LoginForm.defaultProps = {}
export default LoginForm;
