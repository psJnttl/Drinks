import React from 'react';
import PropTypes from 'prop-types';
import SelectEntity from './SelectEntity';
import {Button, Col, Form, FormControl, FormGroup, Glyphicon, Modal, Table} from 'react-bootstrap';
import _ from 'lodash';

class DrinkIngredient extends React.Component {
  constructor(props) {
    super(props);
    this.state = {amount: "", ingredient:{id:0, name:""}}
    this.onChangeAmount = this.onChangeAmount.bind(this);
    this.handleSelectIngredient = this.handleSelectIngredient.bind(this);
    this.buildComponentAndSave = this.buildComponentAndSave.bind(this);
  }

  onChangeAmount(e) {
    const amnt = e.target.value;
    this.setState({amount: amnt}, () => this.buildComponentAndSave());
  }

  handleSelectIngredient(e) {
    const id = parseInt(e.target.value);
    const ingIndex = _.findIndex(this.props.ingredients, (i) => (i.id === id));
    let ing;
    if (-1 === ingIndex) {
      ing = _.assign({}, this.state.ingredient, {id:0, name:""});
    }
    else {
      ing = this.props.ingredients[ingIndex];
      ing = _.assign({}, this.state.ingredient, ing);
    }
    this.setState({ingredient: ing}, () => this.buildComponentAndSave());
  }

  buildComponentAndSave() {
    const component = _.assign({}, {ingredient: this.state.ingredient},
      {value: this.state.amount}, {index: this.props.index});
    this.props.handleIngredient(component);
  }

  componentDidMount() {
    this.setState({amount: this.props.value.value, ingredient:this.props.value.ingredient});
  }

  render() {
    return (
      <tr>
        <td>
          <SelectEntity
            title="Ingredient"
            entityList={this.props.ingredients}
            onSelect={this.handleSelectIngredient}
            value={this.props.value.ingredient}
          />
        </td><td>
          <FormControl type="text" placeholder="amount (cl, dl, oz, ...)" onChange={this.onChangeAmount} value={this.props.value.value} />
        </td><td>
          <Button bsStyle="danger" bsSize="small" onClick={() => this.props.deleteIngredient(this.props.index) } title="delete"><Glyphicon glyph="trash"/></Button>
        </td>
      </tr>
    );
  }
}
DrinkIngredient.PropTypes = {
  ingredients: PropTypes.array.isRequired,
  index: PropTypes.number.isRequired,
  value: PropTypes.object.isRequired,
  handleIngredient: PropTypes.func.isRequired,
  deleteIngredient: PropTypes.func.isRequired,
}
DrinkIngredient.defaultProps = {}
export default DrinkIngredient;
