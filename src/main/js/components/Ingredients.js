import React from 'react';
import PropTypes from 'prop-types';
import SimpleInformationModal from './SimpleInformationModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';
import axios from 'axios';
import _ from 'lodash';
import {Button, Glyphicon, Pagination, Table} from 'react-bootstrap';
import IngredientModal from './IngredientModal';

class Ingredients extends React.Component {
  constructor(props) {
    super(props);
    this.state = {ingredients: [], infoModalVisible: false, addModalVisible:false,
      delConfirmationVisible: false, ingredient: {},
      editModalVisible: false, }
    this.setIngredientList = this.setIngredientList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.setDeleteConfirmModalVisible = this.setDeleteConfirmModalVisible.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.addIngredient = this.addIngredient.bind(this);
  }

  setIngredientList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({ingredients: theList});
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false});
  }

  setDeleteConfirmModalVisible(item) {
    if (false === this.state.delConfirmationVisible) {
      this.setState({delConfirmationVisible: true, ingredient: item});
    }
  }
  deleteReply(answer) {
    if (true === answer) {
      this.deleteIngredient(this.state.ingredient);
    }
    this.setState({delConfirmationVisible: false, ingredient: {} });
  }

  openAddModal() {
    this.setState({addModalVisible: true});
  }

  closeAddModal() {
      this.setState({addModalVisible: false});
  }

  componentDidMount() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/ingredients', config)
         .then(function (response) {
              self.setIngredientList(response.data);
         })
        .catch(function (response) {
          this.setState({infoModalVisible: true});
        });
  }

  render() {
    let addModal;
    if (true === this.state.addModalVisible) {
      addModal =
      <IngredientModal
        modalOpen={this.state.addModalVisible}
        title="Add igredient"
        close={this.closeAddModal}
        save={this.addIngredient}
      />
    }
    else {
      addModal = null;
    }
    const dataRows = this.state.ingredients.map( (row, index) =>
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.name}</td>
        <td>
          <Button bsStyle="danger" bsSize="small" onClick={() => this.setDeleteConfirmModalVisible(row)} title="delete"><Glyphicon glyph="trash"/></Button>

        </td>
      </tr>
    );
    return (
      <div>
        <SimpleInformationModal
          modalOpen={this.state.infoModalVisible}
          header="failedModalHeader"
          title="Fetch ingredients failed"
          notification = "Could not get the list of ingredients from server!"
          notification2 = {this.state.notification2}
          name=""
          reply={this.closeInfoModal} />

        <SimpleConfirmationModal
          modalOpen={this.state.delConfirmationVisible}
          title="Delete ingredient"
          question={"Are you sure you want to delete " + this.state.ingredient.name}
          reply={this.deleteReply}
          header="failedModalHeader"
        />
        {addModal}

        <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add"><Glyphicon glyph="plus"/></Button>

        <Table bordered condensed hover>
          <thead>
            <tr>
              <th>id</th>
              <th>name</th>
              <th>action</th>
            </tr>
          </thead>
          <tbody>
            {dataRows}
          </tbody>
        </Table>
      </div>
    );
  }

  deleteIngredient(ingredient) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const url = 'api/ingredients/' + ingredient.id;
    axios.delete(url, config)
         .then(function (response) {
              console.log("delete ingredient ok");
         })
        .catch(function (response) {
          console.log("delete ingredient failed");
        });
  }

  addIngredient(ingredient) {
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, ingredient);
    axios.post('api/ingredients', command, config)
         .then(function (response) {
              console.log("add ingredient ok");
         })
        .catch(function (response) {
          console.log("add ingredient failed");
        });
  }

}
Ingredients.PropTypes = {}
Ingredients.defaultProps = {}
export default Ingredients;
