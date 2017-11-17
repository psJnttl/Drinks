import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Col, Glyphicon, Pagination, Table} from 'react-bootstrap';
import SimpleInformationModal from './SimpleInformationModal';
import IngredientModal from './IngredientModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';

class Categories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {categories: [], infoModalVisible: false, addModalVisible: false,
    delConfirmationVisible: false, category: {},
    editModalVisible: false, infoModalData: {},
    pgCurrentPage: 1, pgItemsPerPage: 10, searchName: "",
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
    this.openEditModal = this.openEditModal.bind(this);
    this.closeEditModal = this.closeEditModal.bind(this);
    this.modifyCategory = this.modifyCategory.bind(this);
    this.setCurrentPage = this.setCurrentPage.bind(this);
    this.setItemsPerPage = this.setItemsPerPage.bind(this);
    this.paginate = this.paginate.bind(this);
    this.onChangeSearchName = this.onChangeSearchName.bind(this);
  }

  fetchCategories() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/categories', config)
         .then(function (response) {
            self.setCategoryList(response.data);
         })
         .catch(function (response) {
           self.setState({infoModalVisible: true,
               infoModalData: {header:"failedModalHeader",
               title:"Fetch categories failed",
              notification: "Could not get the list of categories from server!",
              name: ""} });
         });
  }

  setCategoryList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({categories: theList});
    this.props.updateRoot(1, theList);
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

  openEditModal(item) {
    this.setState({editModalVisible: true, category: item});
  }

  closeEditModal() {
    this.setState({editModalVisible: false, category: {} });
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

  onChangeSearchName(e) {
    this.setState({searchName: e.target.value, pgCurrentPage: 1});
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
    else if (true === this.state.editModalVisible) {
      addModal =
      <IngredientModal
        modalOpen={this.state.editModalVisible}
        title="Edit category"
        close={this.closeEditModal}
        ingredient={this.state.category}
        save={this.modifyCategory}
      />
    }
    else {
      addModal = null;
    }
    const filtered = this.state.categories.filter(item => item.name.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const pageAmount = Math.ceil(filtered.length / this.state.pgItemsPerPage);
    const itemsOnPage = filtered.filter ( (item, index) => this.paginate(item, index) );

    const dataRows = itemsOnPage.map( (row, index) =>
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
          header={this.state.infoModalData.header}
          title={this.state.infoModalData.title}
          notification = {this.state.infoModalData.notification}
          notification2 =""
          name={this.state.infoModalData.name}
          reply={this.closeInfoModal} />

        <SimpleConfirmationModal
          modalOpen={this.state.delConfirmationVisible}
          title="Delete category"
          question={"Are you sure you want to delete " + this.state.category.name}
          reply={this.deleteReply}
          header="failedModalHeader"
        />

        {addModal}
        <ul style={{'display': 'flex', 'listStyleType': 'none'}} >
          <li>
            <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add"><Glyphicon glyph="plus"/></Button>
          </li>
          <li>
            <input
              className="searchinput"
              type="text"
              placeholder="search by name"
              onChange={ this.onChangeSearchName }
              value={this.state.searchName}
              autoComplete="off"
            />
          </li>
        </ul>
        <Col sm={6}>
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
          /> <br/>
          <Button bsStyle={this.state.pgItemsPerPage === 5 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(5)}>5</Button>
          <Button bsStyle={this.state.pgItemsPerPage === 10 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(10)}>10</Button>
          <Button bsStyle={this.state.pgItemsPerPage === 20 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(20)}>20</Button>
        </Col>
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
              self.fetchCategories();
         })
        .catch(function (response) {
          if (response.response.status === 423) {
            self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Add category failed",
              notification: "Category with same name already exists!",
              name: ""} });
          }
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
          if (response.response.status === 409) {
            self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Delete category failed",
              notification: "Can't delete a category that's in a Drink!",
              name: ""} });
          }
        });
  }

  modifyCategory(category) {
    this.closeEditModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, category);
    const url = 'api/categories/' + category.id;
    axios.put(url, command, config)
         .then(function (response) {
           self.fetchCategories();
         })
         .catch(function (response) {
           if (response.response.status === 400) {
             self.setState({infoModalVisible: true,
               infoModalData: {header:"failedModalHeader",
               title:"Modify category failed",
               notification: "Please check category name next time.",
               name: ""} });
           }
         });
  }


}
Categories.PropTypes = {
  updateRoot: PropTypes.func.isRequired,
}
Categories.defaultProps = {}
export default Categories;
