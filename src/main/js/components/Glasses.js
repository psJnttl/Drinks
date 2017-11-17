import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Col, Glyphicon, Pagination, Table} from 'react-bootstrap';
import IngredientModal from './IngredientModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';
import SimpleInformationModal from './SimpleInformationModal';

class Glasses extends React.Component {
  constructor(props) {
    super(props);
    this.state = {glasses: [], infoModalVisible: false, addModalVisible: false,
        delConfirmationVisible: false, glass:{},
        editModalVisible: false, infoModalData: {},
        pgCurrentPage: 1, pgItemsPerPage: 10,
        searchName: "",
    };
    this.fetchGlasses = this.fetchGlasses.bind(this);
    this.setGlassList = this.setGlassList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.addGlass = this.addGlass.bind(this);
    this.setDeleteConfirmModalVisible = this.setDeleteConfirmModalVisible.bind(this);
    this.deleteGlass = this.deleteGlass.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
    this.openEditModal = this.openEditModal.bind(this);
    this.closeEditModal = this.closeEditModal.bind(this);
    this.modifyGlass = this.modifyGlass.bind(this);
    this.setCurrentPage = this.setCurrentPage.bind(this);
    this.setItemsPerPage = this.setItemsPerPage.bind(this);
    this.paginate = this.paginate.bind(this);
    this.onChangeSearchName = this.onChangeSearchName.bind(this);
  }

  fetchGlasses() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/glasses', config)
         .then(function (response) {
            self.setGlassList(response.data);
         })
        .catch(function (response) {
          self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Fetch glasses failed",
             notification: "Could not get the list of glasses from server!",
             name: ""} });
        });
  }

  setGlassList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({glasses: theList});
    this.props.updateRoot(2, theList);
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false, infoModalData: {}});
  }

  openAddModal() {
    this.setState({addModalVisible: true});
  }

  closeAddModal() {
    this.setState({addModalVisible: false});
  }

  setDeleteConfirmModalVisible(item) {
    if (false === this.state.delConfirmationVisible) {
      this.setState({delConfirmationVisible: true, glass: item});
    }
  }

  deleteReply(answer) {
    if (true === answer) {
      this.deleteGlass(this.state.glass);
    }
    this.setState({delConfirmationVisible: false, glass: {} });
  }

  openEditModal(item) {
    this.setState({editModalVisible: true, glass: item});
  }

  closeEditModal() {
    this.setState({editModalVisible: false, glass: {} });
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
    this.fetchGlasses();
  }

  render() {
    let addModal;
    if (true === this.state.addModalVisible) {
      addModal =
      <IngredientModal
        modalOpen={this.state.addModalVisible}
        title="Add glass"
        close={this.closeAddModal}
        save={this.addGlass}
        placeholder="glass name"
      />
    }
    else if (true === this.state.editModalVisible) {
      addModal =
      <IngredientModal
        modalOpen={this.state.editModalVisible}
        title="Edit glass"
        close={this.closeEditModal}
        ingredient={this.state.glass}
        save={this.modifyGlass}
      />
    }
    else {
      addModal = null;
    }

    const filtered = this.state.glasses.filter(item => item.name.toLowerCase().includes(this.state.searchName.toLowerCase()));
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
          title="Delete glass"
          question={"Are you sure you want to delete " + this.state.glass.name}
          reply={this.deleteReply}
          header="failedModalHeader"
        />
        {addModal}
        <ul style={{'display': 'flex', 'listStyleType': 'none'}}>
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
            autoComplete="off" />
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

  addGlass(glass) {
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, glass);
    axios.post('api/glasses', command, config)
         .then(function (response) {
              self.fetchGlasses();
         })
        .catch(function (response) {
          if (response.response.status === 423) {
            self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Add glass failed",
              notification: "Glass with same name already exists!",
              name: ""} });
          }
        });
  }

  deleteGlass(glass) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const url = 'api/glasses/' + glass.id;
    axios.delete(url, config)
         .then(function (response) {
              self.fetchGlasses();
         })
        .catch(function (response) {
           if (response.response.status === 409) {
             self.setState({infoModalVisible: true,
               infoModalData: {header:"failedModalHeader",
               title:"Delete glass failed",
               notification: "Can't delete a glass holding in a Drink!",
               name: ""} });
           }
        });
  }

  modifyGlass(glass) {
    this.closeEditModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, glass);
    const url = 'api/glasses/' + glass.id;
    axios.put(url, command, config)
         .then(function (response) {
           self.fetchGlasses();
         })
         .catch(function (response) {
           console.log("modify glass failed");
         });
  }

}
Glasses.PropTypes = {
  updateRoot: PropTypes.func.isRequired,
}
Glasses.defaultProps = {}
export default Glasses;
