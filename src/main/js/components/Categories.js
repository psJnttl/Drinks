import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon, Table} from 'react-bootstrap';
import SimpleInformationModal from './SimpleInformationModal';
import IngredientModal from './IngredientModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';

class Categories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {categories: [], infoModalVisible: false, addModalVisible: false,
    delConfirmationVisible: false, category: {}
  };
    this.fetchCategories = this.fetchCategories.bind(this);
    this.setCategoryList = this.setCategoryList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.addCategory = this.addCategory.bind(this);
    this.setDeleteConfirmModalVisible = this.setDeleteConfirmModalVisible.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
    this.deleteCategory = this.deleteCategory.bind(this);
  }

  fetchCategories() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/categories', config)
         .then(function (response) {
            self.setCategoryList(response.data);
         })
         .catch(function (response) {
            self.setState({infoModalVisible: true});
         });
  }

  setCategoryList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({categories: theList});
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false});
  }

  openAddModal() {
    this.setState({addModalVisible: true})
  }

  closeAddModal() {
    this.setState({addModalVisible: false});
  }

  setDeleteConfirmModalVisible(item) {
    if (false === this.state.delConfirmationVisible) {
      this.setState({delConfirmationVisible: true, category: item});
    }
  }

  deleteReply(answer) {
    if (true === answer) {
      this.deleteCategory(this.state.category);
    }
    this.setState({delConfirmationVisible: false, category: {} });
  }

  componentDidMount() {
    this.fetchCategories();
  }

  render() {
    let addModal;
    if (true === this.state.addModalVisible) {
      addModal =
      <IngredientModal
        modalOpen={this.state.addModalVisible}
        title="Add category"
        close={this.closeAddModal}
        save={this.addCategory}
        placeholder="category name"
      />
    }
    else {
      addModal = null;
    }
    const dataRows = this.state.categories.map( (row, index) =>
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.name}</td>
        <td>
          <Button bsStyle="danger" bsSize="small" onClick={() => this.setDeleteConfirmModalVisible(row)} title="delete"><Glyphicon glyph="trash"/></Button>
          <Button bsStyle="warning" bsSize="small" onClick={() => this.openEditModal(row)} title="edit"><Glyphicon glyph="pencil"/></Button>
        </td>
      </tr>
    );

    return (
      <div>
        <SimpleInformationModal
          modalOpen={this.state.infoModalVisible}
          header="failedModalHeader"
          title="Fetch categories failed"
          notification = "Could not get the list of categories from server!"
          name=""
          reply={this.closeInfoModal} />

        <SimpleConfirmationModal
          modalOpen={this.state.delConfirmationVisible}
          title="Delete category"
          question={"Are you sure you want to delete " + this.state.category.name}
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

  addCategory(category) {
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, category);
    axios.post('api/categories', command, config)
         .then(function (response) {
              console.log("add category ok");
              const cats = _.concat(self.state.categories, response.data);
              self.setState({categories: cats});
         })
        .catch(function (response) {
          console.log("add category failed");
        });
  }

  deleteCategory(category) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const url = 'api/categories/' + category.id;
    axios.delete(url, config)
         .then(function (response) {
              self.fetchCategories();
         })
        .catch(function (response) {
          console.log("delete category failed");
        });
  }




}
Categories.PropTypes = {}
Categories.defaultProps = {}
export default Categories;
