import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Col, Glyphicon, Pagination} from 'react-bootstrap';
import DrinkModal from './DrinkModal';
import _ from 'lodash';
import SimpleInformationModal from './SimpleInformationModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';

class Drinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drinks: [], infoModalVisible: false,  addModalVisible: false,
      infoModalData: {}, editModalVisible: false, drink: {},
      delConfirmationVisible: false,
      pgCurrentPage: 1, pgItemsPerPage: 10, searchName: "",
    };
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
    this.setCurrentPage = this.setCurrentPage.bind(this);
    this.setItemsPerPage = this.setItemsPerPage.bind(this);
    this.paginate = this.paginate.bind(this);
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
          if (response.response.status === 423) {
            self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Add drink failed",
              notification: "Drink with same name already exists!",
              name: ""} });
          }
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

  setCurrentPage(pageNbr) {
    this.setState({pgCurrentPage: pageNbr})
  }

  setItemsPerPage(nbrItems) {
    this.setState({pgItemsPerPage: nbrItems, pgCurrentPage: 1});
  }

  paginate (item, index) {
    return (index >= (this.state.pgCurrentPage-1) * this.state.pgItemsPerPage) &&
      (index < (this.state.pgCurrentPage-1) * this.state.pgItemsPerPage + this.state.pgItemsPerPage);
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

    const filtered = this.state.drinks.filter(item => item.name.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const sorted = _.orderBy(filtered, [function(d) { return d.name.toLowerCase(); }], ['asc']);

    const pageAmount = Math.ceil(sorted.length / this.state.pgItemsPerPage);
    const itemsOnPage = sorted.filter ( (item, index) => this.paginate(item, index) );

    const drinkList =  itemsOnPage.length === 0 ?
                       null :
    <ul style={{'display': 'flex', 'flexWrap': 'wrap', 'fontFamily': 'effra', 'listStyleType': 'none'}}>{
      itemsOnPage.map( (drink, index) =>
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

        <ul style={{'display': 'flex', 'listStyleType': 'none'}}>
          <li>
            <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add Drink"><Glyphicon glyph="plus"/></Button>
          </li>
        </ul>
        <Col sm={11}>
          {drinkList}
          <Pagination
            bsSize="medium"
            items={ pageAmount }
            activePage={this.state.pgCurrentPage}
            onSelect={ this.setCurrentPage}
            prev
            next
            boundaryLinks
            ellipsis
            maxButtons={5}
          /><br/>
          <Button bsStyle={this.state.pgItemsPerPage === 10 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(10)}>10</Button>
          <Button bsStyle={this.state.pgItemsPerPage === 20 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(20)}>20</Button>
          <Button bsStyle={this.state.pgItemsPerPage === 40 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(40)}>40</Button>
        </Col>
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
