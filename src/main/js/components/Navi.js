import React from 'react';
import PropTypes from 'prop-types';
import {Nav, Navbar, NavItem} from 'react-bootstrap';
import {Link, NavLink, Route} from 'react-router-dom';

class RouteNavItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  render() {
    return (
      <Route
        path={this.props.href}
        exact
        children={({ match, history }) =>
          <NavItem
            onClick={e => history.push(e.currentTarget.getAttribute("href"))}
            {...this.props}
            active={match ? true : false}
          >
            {this.props.children}
          </NavItem>}
      />
    );
  }
}
RouteNavItem.PropTypes = {}
RouteNavItem.defaultProps = {}

class Navi extends React.Component {
  constructor(props) {
    super(props);
    this.state = {activeTab: "/"}
    this.selectActive = this.selectActive.bind(this);
  }

  selectActive(tab) {
    this.setState({activeTab: tab});
  }

  render() {
    return (
      <Navbar fluid collapseOnSelect>
        <Navbar.Header>

        </Navbar.Header>
        <Navbar.Toggle />
        <Navbar.Collapse>
          <Nav>
            <RouteNavItem href="/">Home</RouteNavItem>
            <RouteNavItem href="/ingredients">Ingredients</RouteNavItem>
            <RouteNavItem href="/glasses">Glasses</RouteNavItem>
            <RouteNavItem href="/about">About</RouteNavItem>
            {this.props.authenticated &&
              <RouteNavItem href="/logout">logout</RouteNavItem>
            }
          </Nav>
        </Navbar.Collapse>
      </Navbar>

    );
  }
}
Navi.PropTypes = {
  authenticated: PropTypes.bool.isRequired,
  changeAuthState: PropTypes.func.isRequired,
}
Navi.defaultProps = {}
export default Navi;
