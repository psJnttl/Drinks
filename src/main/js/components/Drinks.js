import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon} from 'react-bootstrap';
import DrinkModal from './DrinkModal';
import _ from 'lodash';
import SimpleInformationModal from './SimpleInformationModal';

class Drinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drinks: [], infoModalVisible: false,  addModalVisible: false,
      infoModalData: {} };
    this.fetchDrinks = this.fetchDrinks.bind(this);
    this.setDrinkList = this.setDrinkList.bind(this);
    this.drinkClick = this.drinkClick.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.sendNewDrinkToServer = this.sendNewDrinkToServer.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
  }

  fetchDrinks() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/drinks', config)
         .then(function (response) {
            self.setDrinkList(response.data);
         })
         .catch(function (response) {
           self.setState({infoModalVisible: true,
               infoModalData: {header:"failedModalHeader",
               title:"Fetch drinks failed",
              notification: "Could not get the list of drinks from server!",
              name: ""} });
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

  closeInfoModal() {
    this.setState({infoModalVisible: false, infoModalData: {}});
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
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, drink);
    axios.post('api/drinks', command, config)
         .then(function (response) {
              const drnks = _.concat(self.state.drinks, response.data);
              self.setState({drinks: drnks});
         })
        .catch(function (response) {
          console.log("add drink failed");
        });
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
        glasses={this.props.glasses}
        ingredients={this.props.ingredients}
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
        <SimpleInformationModal
          modalOpen={this.state.infoModalVisible}
          header={this.state.infoModalData.header}
          title={this.state.infoModalData.title}
          notification = {this.state.infoModalData.notification}
          notification2 =""
          name={this.state.infoModalData.name}
          reply={this.closeInfoModal} />

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
