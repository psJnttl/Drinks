import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon} from 'react-bootstrap';
import DrinkModal from './DrinkModal';
import _ from 'lodash';

class Drinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drinks: [], infoModalVisible: false,  addModalVisible: false, };
    this.fetchDrinks = this.fetchDrinks.bind(this);
    this.setDrinkList = this.setDrinkList.bind(this);
    this.drinkClick = this.drinkClick.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.sendNewDrinkToServer = this.sendNewDrinkToServer.bind(this);

  }

  fetchDrinks() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/drinks', config)
         .then(function (response) {
            self.setDrinkList(response.data);
         })
         .catch(function (response) {
            self.setState({infoModalVisible: true});
         });
  }

  setDrinkList(data) {
    const theList = data.map( item =>
       _.assign({}, {
         id: item.id,
         name: item.name,
         category: item.category,
         glass: item.glass,
         components: item.components
       }) );
    this.setState({drinks: theList});
  }

  drinkClick(id) {
    console.log("drink id: " + id)
  }

  openAddModal() {
    this.setState({addModalVisible: true})
  }

  closeAddModal() {
    this.setState({addModalVisible: false});
  }

  sendNewDrinkToServer(drink) {
    console.log("sendNewDrinkToServer");
    console.log(drink.name);
  }



  componentDidMount() {
    this.fetchDrinks();
  }

  render() {
    let drinkModal;
    if (true === this.state.addModalVisible) {
      drinkModal =
      <DrinkModal
        modalOpen={this.state.addModalVisible}
        close={this.closeAddModal}
        title="Add a Drink"
        save={this.sendNewDrinkToServer}
        categories={this.props.categories}
      />
    }
    else {
      drinkModal = null;
    }
    const drinkList =  this.state.drinks.length === 0 ?
                       null :
    <ul style={{'display': 'flex', 'cursor': 'pointer', 'font-family': 'effra'}}>{
      this.state.drinks.map( (drink, index) =>
        <div key={drink.id} style={{ 'padding': '10px'}} onClick={() => this.drinkClick(drink.id)}>
          <div><h3>{drink.name}</h3></div>
          <div title="kategoria" style={{'background': '#e3f2fd'}}><h5>{drink.category.name}</h5></div>
          <div title="lasi" style={{'background': '#bbdefb'}}><h5>{drink.glass.name}</h5></div>
        </div>
      )
    }</ul>
    return (
      <div>
        <h4>Drinks listed here</h4>
        <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add Drink"><Glyphicon glyph="plus"/></Button>
        {drinkList}
        {drinkModal}
      </div>
    );
  }
}
Drinks.PropTypes = {
  categories: PropTypes.array.isRequired,
  glasses: PropTypes.array.isRequired,
  ingredients: PropTypes.array.isRequired,
}
Drinks.defaultProps = {}
export default Drinks;
