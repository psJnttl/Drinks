import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Modal} from 'react-bootstrap';
import _ from 'lodash';
import SelectEntity from './SelectEntity';

class DrinkModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drink: {id:0, name: "", category: {id:0, name:""},
                  glass: {id:0, name:""}, components: []},
                 }
    this.onChangeName = this.onChangeName.bind(this);
    this.handleSelectCategory = this.handleSelectCategory.bind(this);
    this.handleSelectGlass = this.handleSelectGlass.bind(this);
  }

  onChangeName(e) {
    const drinkk = _.assign({}, this.state.drink, {name: e.target.value});
    this.setState({drink: drinkk});
  }

  handleSelectCategory(e) {
    const id = parseInt(e.target.value);
    const catIndex = _.findIndex(this.props.categories, (c) => (c.id === id) );
    let drink;
    if (-1 === catIndex) {
      drink = _.assign({}, this.state.drink, {category: {id:0, name:""}});
    }
    else {
      const cat = this.props.categories[catIndex];
      drink = _.assign({}, this.state.drink, {category: cat});
    }
    this.setState({drink: drink});
  }

  handleSelectGlass(e) {
    const id = parseInt(e.target.value);
    const glassIndex = _.findIndex(this.props.glasses, (g) => (g.id === id));
    let drink;
    if (-1 === glassIndex) {
      drink = _.assign({}, this.state.drink, {glass: {id: 0, name: ""}});
    }
    else {
      const glss = this.props.glasses[glassIndex];
      drink = _.assign({}, this.state.drink, {glass: glss});
    }
    this.setState({drink: drink});
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
            <FormGroup controlId="formInput">
              <FormControl type="text" placeholder="drink name" onChange={this.onChangeName} value={this.state.drink.name} />
            </FormGroup>
            <FormGroup controlId="formSelect">
              <SelectEntity
                title="Category"
                entityList={this.props.categories}
                onSelect={this.handleSelectCategory}
                value={this.state.drink.category}
              />
            </FormGroup>
            <FormGroup controlId="formSelect">
              <SelectEntity
                title="Glass"
                entityList={this.props.glasses}
                onSelect={this.handleSelectGlass}
                value={this.state.drink.glass}
              />
            </FormGroup>
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
  categories: PropTypes.array.isRequired,
  glasses: PropTypes.array.isRequired,
}
DrinkModal.defaultProps = {
  drink: {id:0, name: "", category: {id:0, name:""}, glass: {id:0, name:""}, components: []},
  headerStyle: "successModalHeader",
}
export default DrinkModal;
