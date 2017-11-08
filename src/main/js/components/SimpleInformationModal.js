import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Modal} from 'react-bootstrap';

class SimpleInformationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  render() {
    let note1;
    if (this.props.name === "") {
      note1 = <strong>{this.props.notification}</strong>
    }
    else {
      note1 = <strong>{this.props.notification} '{this.props.name}' !</strong>
    }
    return (
      <div>
        <Modal bsSize="small" show={this.props.modalOpen} onHide={() => this.props.reply(false)}>
          <Modal.Header closeButton bsClass={this.props.header}>
            <Modal.Title>{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {note1} <br />
            {this.props.notification2}
          </Modal.Body>
          <Modal.Footer bsClass="modalFooter">
            <Button bsStyle="success" onClick={ () => this.props.reply(true) }>OK</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
SimpleInformationModal.PropTypes = {
  modalOpen: PropTypes.bool.isRequired,
  title: PropTypes.string,
  notification: PropTypes.string,
  notification2: PropTypes.string,
  name: PropTypes.string,
  reply: PropTypes.func.isRequired,
  header: PropTypes.string,
}
SimpleInformationModal.defaultProps = {
  title: "Login has failed",
  notification: "Failed to login user: ",
  notification2: "",
  name: "username",
  header: "failedModalHeader",
}
export default SimpleInformationModal;
