import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Modal} from 'react-bootstrap';
import _ from 'lodash';

class IngredientModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {ingredient: {id:0, name:""} }
    this.onChangeName = this.onChangeName.bind(this);
  }

  onChangeName(e) {
    const ing = _.assign({}, this.state.ingredient, {name: e.target.value});
    this.setState({ingredient: ing});
  }

  componentDidMount() {
    console.log("IngredientModal.componentDidMount");
    const ing = _.assign({}, this.props.ingredient);
    this.setState({ingredient: ing});
  }
  componentWillUnmount() {
    console.log("componentWillUnmount");
  }

  render() {
    if (this.props.modalOpen === false) {
      return null;
    }
    return (
      <div>
        <Modal bsSize="small" show={this.props.modalOpen} onHide={() => this.props.close()}>
          <Modal.Header closeButton bsClass={this.props.header}>
            <Modal.Title>{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <FormControl type="text" placeholder={this.props.placeholder} onChange={this.onChangeName} value={this.state.ingredient.name} />
          </Modal.Body>
          <Modal.Footer bsClass="modalFooter">
            <Button bsStyle="success" onClick={ () => this.props.save(this.state.ingredient) }
              disabled={! (this.state.ingredient.name.length > 0)} >Save</Button>
            <Button bsStyle="danger" onClick={ () => this.props.close() }>Cancel</Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
IngredientModal.PropTypes = {
  modalOpen: PropTypes.bool.isRequired,
  title: PropTypes.string,
  save: PropTypes.func.isRequired,
  close: PropTypes.func.isRequired,
  header: PropTypes.string,
  ingredient: PropTypes.object,
  placeholder: PropTypes.string,
}
IngredientModal.defaultProps = {
  title: "Add ingredient",
  header: "successModalHeader",
  ingredient: {id:0, name:""},
  placeholder: "ingredient name",
}
export default IngredientModal;
