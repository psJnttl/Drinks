import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Glyphicon, Modal, Table} from 'react-bootstrap';

class UserRoles extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    const dataRows = this.props.roles.map( (item, index) =>
      <tr key={index}>
        <td>{item.id}</td>
        <td>{item.name}</td>
        <td>
          <Button bsStyle="danger" bsSize="small" onClick={() => this.props.delete(item)} title="delete role"><Glyphicon glyph="trash"/></Button>
        </td>
      </tr>
    );
    return (
      <div>
        <Table condensed>
          <thead>
            <tr>
              <th>id</th>
              <th>role name</th>
              <th>action</th>
            </tr>
          </thead>
          <tbody>
            {dataRows}
          </tbody>
        </Table>
      </div>
    );
  }
}
UserRoles.PropTypes = {
  roles: PropTypes.array.isRequired,
  delete: PropTypes.func.isRequired,
}
UserRoles.defaultProps = {}
export default UserRoles;
