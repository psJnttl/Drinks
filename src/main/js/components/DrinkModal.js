import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Modal} from 'react-bootstrap';
import _ from 'lodash';

class DrinkModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drink: {id:0, name: "", category: {id:0, name:""},
                  glass: {id:0, name:""}, components: []},
                 }
    this.onChangeName = this.onChangeName.bind(this);
  }
  onChangeName(e) {
    const drinkk = _.assign({}, this.state.drink, {name: e.target.value});
    this.setState({drink: drinkk});
  }
  render() {
    if (false === this.props.modalOpen) {
      return null;
    }
    return (
      <div>
        <Modal bsSize="large" show={this.props.modalOpen} onHide={() => this.props.close()}>
          <Modal.Header closeButton bsClass={this.props.headerStyle}>
            <Modal.Title>{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <FormControl type="text" placeholder="drink name" onChange={this.onChangeName} value={this.state.drink.name} />
          </Modal.Body>
          <Modal.Footer bsClass="modalFooter">
            <Button bsStyle="success" onClick={ () => this.props.save(this.state.drink) }>Save</Button>
            <Button bsStyle="danger" onClick={ () => this.props.close() }>Cancel</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
DrinkModal.PropTypes = {
  modalOpen: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
  title: PropTypes.string,
  headerStyle: PropTypes.string,
  drink: PropTypes.object,
  save: PropTypes.func.isRequired,
}
DrinkModal.defaultProps = {
  drink: {id:0, name: "", category: {id:0, name:""}, glass: {id:0, name:""}, components: []},
  headerStyle: "successModalHeader",
}
export default DrinkModal;
