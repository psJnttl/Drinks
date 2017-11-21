import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, FormGroup, Nav, NavItem, Row, Tab} from 'react-bootstrap';
import EventLog from './EventLog';
import Users from './Users';

class Admin extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
    this.method = this.method.bind(this);
  }
  method() {}
  render() {
    return (
      <div style={{margin: 20}}>
        <Tab.Container id="tabs-with-dropdown" defaultActiveKey="logsTab">
          <Row className="clearfix">
            <Nav bsStyle="tabs">
              <NavItem eventKey="logsTab">
                Event Log
              </NavItem>
              <NavItem eventKey="usersTab">
                Users
              </NavItem>
            </Nav>
            <Tab.Content animation>
              <Tab.Pane eventKey="logsTab">
                <EventLog />
              </Tab.Pane>
              <Tab.Pane eventKey="usersTab">
                <Users />
              </Tab.Pane>
            </Tab.Content>

          </Row>
        </Tab.Container>
      </div>
    );
  }
}
Admin.PropTypes = {
  authState: PropTypes.object.isRequired,
}
Admin.defaultProps = {}
export default Admin;
