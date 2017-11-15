import React from 'react';
import PropTypes from 'prop-types';
import SelectEntity from './SelectEntity';
import {Button, Col, Form, FormControl, FormGroup, Glyphicon, Modal, Table} from 'react-bootstrap';
import DrinkIngredient from './DrinkIngredient';

class DrinkIngredients extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    const dataRows = this.props.value.map((row, index) =>
      <DrinkIngredient
        ingredients={this.props.ingredients}
        value={this.props.value[index]}
        index={index}
        handleIngredient={this.props.handleIngredient}
        deleteIngredient={this.props.deleteIngredient}
      />
    );
    return (
      <div>
        <Table condensed hover>
          <thead>
            <tr>
              <th>ingredient</th>
              <th>amount</th>
              <th>action</th>
            </tr>
          </thead>
          <tbody>
            {dataRows}
          </tbody>
        </Table>

        <Button bsStyle="success" onClick={ () => this.props.addIngredient() } title="add Ingredient"><Glyphicon glyph="plus"/></Button>
      </div>
    );
  }
}
DrinkIngredients.PropTypes = {
  ingredients: PropTypes.array.isRequired,
  value: PropTypes.array.isRequired,
  handleIngredient : PropTypes.func.isRequired,
  addIngredient: PropTypes.func.isRequired,
  deleteIngredient: PropTypes.func.isRequired,
}
DrinkIngredients.defaultProps = {}
export default DrinkIngredients;
