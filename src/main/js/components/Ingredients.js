import React from 'react';
import PropTypes from 'prop-types';
import SimpleInformationModal from './SimpleInformationModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';
import axios from 'axios';
import _ from 'lodash';
import {Button, Glyphicon, Pagination, Table} from 'react-bootstrap';


class Ingredients extends React.Component {
  constructor(props) {
    super(props);
    this.state = {ingredients: [], infoModalOpen: false, addModalVisible:false,
      delConfirmationVisible: false, ingredient: {}}
    this.setIngredientList = this.setIngredientList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.toggleAddModal = this.toggleAddModal.bind(this);
    this.setDeleteConfirmModalVisible = this.setDeleteConfirmModalVisible.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
  }

  setIngredientList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({ingredients: theList});
  }

  closeInfoModal() {
    this.setState({infoModalOpen: false});
  }

  toggleAddModal() {
    this.setState({addModalVisible: !this.state.addModalVisible});
  }

  setDeleteConfirmModalVisible(item) {
    if (false === this.state.delConfirmationVisible) {
      this.setState({delConfirmationVisible: true, ingredient: item});
    }
  }
  deleteReply(answer) {
    this.setState({delConfirmationVisible: false, ingredient: {} });
  }
  componentDidMount() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/ingredients', config)
         .then(function (response) {
              self.setIngredientList(response.data);
         })
        .catch(function (response) {
          this.setState({infoModalOpen: true});
        });
  }

  render() {
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
          modalOpen={this.state.infoModalOpen}
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

        <Button bsStyle="success" onClick={ () => this.toggleAddModal() } title="add"><Glyphicon glyph="plus"/></Button>

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
    axios.delete(ulr, config)
         .then(function (response) {
              console.log("delete ingredient ok");
         })
        .catch(function (response) {
          console.log("delete ingredient failed");
        });
  }

}
Ingredients.PropTypes = {}
Ingredients.defaultProps = {}
export default Ingredients;
