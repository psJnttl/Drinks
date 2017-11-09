import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Modal} from 'react-bootstrap';

class SimpleConfirmationModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }
  render() {
    if (! this.props.modalOpen) {
      return null;
    }
    return (
      <div>
        <Modal bsSize="small" show={this.props.modalOpen} onHide={() => this.props.reply(false)}>
          <Modal.Header closeButton bsClass={this.props.header}>
            <Modal.Title>{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <strong>{this.props.question}?</strong><br />
          </Modal.Body>
          <Modal.Footer bsClass="modalFooter">
            <Button bsStyle="success" onClick={ () => this.props.reply(true) }>OK</Button>
            <Button bsStyle="danger" onClick={ () => this.props.reply(false) }>Cancel</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
SimpleConfirmationModal.PropTypes = {
  modalOpen: PropTypes.bool.isRequired,
  title: PropTypes.string,
  question: PropTypes.string,
  reply: PropTypes.func.isRequired,
  header: PropTypes.string,
}
SimpleConfirmationModal.defaultProps = {
  title: "Please confirm",
  question: "Are you sure",
  header: "failedModalHeader",
}
export default SimpleConfirmationModal;
