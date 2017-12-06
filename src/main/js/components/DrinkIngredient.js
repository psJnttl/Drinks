import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, Glyphicon, Modal, Table} from 'react-bootstrap';
import _ from 'lodash';
import Select from 'react-select';

class DrinkIngredient extends React.Component {
  constructor(props) {
    super(props);
    this.state = {amount: "", ingredient:{id:0, name:""}}
    this.onChangeAmount = this.onChangeAmount.bind(this);
    this.handleSelectIngredient = this.handleSelectIngredient.bind(this);
    this.buildComponentAndSave = this.buildComponentAndSave.bind(this);
    this.selected = this.selected.bind(this);
    this.filterItem = this.filterItem.bind(this);
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
    const component = _.assign({},
      {ingredient: this.state.ingredient.value},
      {value: this.state.amount}, {index: this.props.index});
    this.props.handleIngredient(component);
  }

  transformListForSelect(itemList) {
    return  itemList.map( item =>
       _.assign({}, {value: item, label: item.name})
    );
  }

  selected(item) {
    console.log(item);
    if (undefined !== item.value) {
      this.setState({ingredient: item}, () => this.buildComponentAndSave());
    }
  }

  filterItem(options, filter) {
    if (filter.length < 2) {
      return ;
    }
     return options.label.toLowerCase().includes(filter.toLowerCase());
  }

  componentDidMount() {
    const item = _.assign({}, {value: this.props.value.ingredient, label: this.props.value.ingredient.name});
    this.setState({amount: this.props.value.value, ingredient: item});
  }

  render() {
    const options = this.transformListForSelect(this.props.ingredients)
    return (
      <tr>
        <td>
          <Select
            name="ingredientSelect"
            value={this.state.ingredient}
            options={options}
            onChange={this.selected}
            autosize={false}
            clearable={false}
            placeholder="search ingredients"
          />
        </td><td>
          <FormControl type="text" placeholder="amount (cl, dl, oz, ...)" onChange={this.onChangeAmount} value={this.state.amount} />
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
