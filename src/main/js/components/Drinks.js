import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon} from 'react-bootstrap';
import DrinkModal from './DrinkModal';
import _ from 'lodash';
import SimpleInformationModal from './SimpleInformationModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';

class Drinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drinks: [], infoModalVisible: false,  addModalVisible: false,
      infoModalData: {}, editModalVisible: false, drink: {},
      delConfirmationVisible: false, };
    this.fetchDrinks = this.fetchDrinks.bind(this);
    this.setDrinkList = this.setDrinkList.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.sendNewDrinkToServer = this.sendNewDrinkToServer.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.editDrink = this.editDrink.bind(this);
    this.sendOldDrinkToServer = this.sendOldDrinkToServer.bind(this);
    this.closeEditModal = this.closeEditModal.bind(this);
    this.setDeleteConfirmModalVisible = this.setDeleteConfirmModalVisible.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
    this.deleteDrink = this.deleteDrink.bind(this);
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

  editDrink(id) {
    console.log("drink id: " + id)
    const drinksIndex = _.findIndex(this.state.drinks, (d) => (d.id === id) );
    if (-1 !== drinksIndex) {
      const drink = this.state.drinks[drinksIndex];
      this.setState({editModalVisible: true, drink: drink});
    }
  }

  closeEditModal() {
    this.setState({editModalVisible: false, drink: {}});
  }

  sendOldDrinkToServer(drink) {
    this.closeEditModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, drink);
    const url = 'api/drinks/' + drink.id;
    axios.put(url, command, config)
         .then(function (response) {
           self.fetchDrinks();
         })
         .catch(function (response) {
           console.log("modify drink failed");
         });
  }

  setDeleteConfirmModalVisible(item) {
    this.setState({delConfirmationVisible: true, drink: item});
  }

  deleteReply(answer) {
    if (true === answer) {
      this.deleteDrink(this.state.drink);
    }
    this.setState({delConfirmationVisible: false, drink: {} });
  }

  deleteDrink(drink) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const url = 'api/drinks/' + drink.id;
    axios.delete(url, config)
         .then(function (response) {
              self.fetchDrinks();
         })
        .catch(function (response) {
           self.setState({infoModalVisible: true,
               infoModalData: {header:"failedModalHeader",
               title:"Delete drink failed",
               notification: "Can't delete a Drink due to unknown reason!",
               name: ""} });

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
    else if (true ===this.state.editModalVisible) {
      drinkModal =
      <DrinkModal
        modalOpen={this.state.editModalVisible}
        close={this.closeEditModal}
        title="Edit a Drink"
        save={this.sendOldDrinkToServer}
        categories={this.props.categories}
        glasses={this.props.glasses}
        ingredients={this.props.ingredients}
        drink={this.state.drink}
      />
    }
    else {
      drinkModal = null;
    }
    const drinkList =  this.state.drinks.length === 0 ?
                       null :
    <ul style={{'display': 'flex', 'fontFamily': 'effra', 'listStyleType': 'none'}}>{
      this.state.drinks.map( (drink, index) =>
        <li key={drink.id} style={{ 'padding': '10px', 'border': 'solid', 'margin': '10px'}} >
          <div title="lasi" style={{'background': 'white', 'textAlign': 'right'}}>
            <Button bsStyle="danger" bsSize="xsmall" onClick={ () => this.setDeleteConfirmModalVisible(drink) } title="delete Drink">X</Button>
          </div>
          <div style={{'cursor': 'pointer'}}  onClick={() => this.editDrink(drink.id)}>
            <div><h3>{drink.name}</h3></div>
            <div title="kategoria" style={{'background': '#e3f2fd', 'textAlign': 'center'}}><h5>{drink.category.name}</h5></div>
            <div title="lasi" style={{'background': '#bbdefb', 'textAlign': 'center'}}>
              <h5>{drink.glass.name}</h5>
            </div>
          </div>

        </li>
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

        <SimpleConfirmationModal
          modalOpen={this.state.delConfirmationVisible}
          title="Delete drink"
          question={"Are you sure you want to delete " + this.state.drink.name}
          reply={this.deleteReply}
          header="failedModalHeader"
        />

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
