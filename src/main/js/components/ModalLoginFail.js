import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Modal} from 'react-bootstrap';

class ModalLoginFail extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  render() {
    return (
      <div>
        <Modal bsSize="small" show={this.props.modalOpen} onHide={() => this.props.reply(false)}>
          <Modal.Header closeButton bsClass="loginFailedHeader">
            <Modal.Title>{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <strong>{this.props.notification} '{this.props.name}' !</strong>
          </Modal.Body>
          <Modal.Footer bsClass="modalFooter">
            <Button bsStyle="danger" onClick={ () => this.props.reply(true) }>OK</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
ModalLoginFail.PropTypes = {
  modalOpen: PropTypes.bool.isRequired,
  title: PropTypes.string,
  notification: PropTypes.string,
  name: PropTypes.string,
  reply: PropTypes.func.isRequired,
}
ModalLoginFail.defaultProps = {
  title: "Login has failed",
  notification: "Failed to login user: ",
  name: "username",
}
export default ModalLoginFail;
