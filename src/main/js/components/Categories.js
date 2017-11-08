import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon, Table} from 'react-bootstrap';
import SimpleInformationModal from './SimpleInformationModal';
import IngredientModal from './IngredientModal';

class Categories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {categories: [], infoModalVisible: false, addModalVisible: false,
    };
    this.fetchCategories = this.fetchCategories.bind(this);
    this.setCategoryList = this.setCategoryList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.addCategory = this.addCategory.bind(this);
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
}
Categories.PropTypes = {}
Categories.defaultProps = {}
export default Categories;
