import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon, Table} from 'react-bootstrap';
import IngredientModal from './IngredientModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';

class Glasses extends React.Component {
  constructor(props) {
    super(props);
    this.state = {glasses: [], infoModalVisible: false, addModalVisible: false,
        delConfirmationVisible: false, glass:{},
        editModalVisible: false,
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
  }

  fetchGlasses() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/glasses', config)
         .then(function (response) {
              self.setGlassList(response.data);
         })
        .catch(function (response) {
          this.setState({infoModalVisible: true});
        });
  }

  setGlassList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({glasses: theList});
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false});
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
    const dataRows = this.state.glasses.map( (row, index) =>
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
        <SimpleConfirmationModal
          modalOpen={this.state.delConfirmationVisible}
          title="Delete glass"
          question={"Are you sure you want to delete " + this.state.glass.name}
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

  addGlass(glass) {
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, glass);
    axios.post('api/glasses', command, config)
         .then(function (response) {
              console.log("add glass ok");
              const glsss = _.concat(self.state.glasses, response.data);
              self.setState({glasses: glsss});
         })
        .catch(function (response) {
          console.log("add glass failed");
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
          console.log("delete glass failed");
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
Glasses.PropTypes = {}
Glasses.defaultProps = {}
export default Glasses;
