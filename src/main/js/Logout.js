import React from 'react';
import PropTypes from 'prop-types';
import SimpleConfirmationModal from './components/SimpleConfirmationModal';
import NetworkApi from './components/NetworkApi';

class Logout extends React.Component {
  constructor(props) {
    super(props);
    this.state = {modalOpen: true, logout:false}
    this.toggleModal = this.toggleModal.bind(this);
  }
  toggleModal(answer) {
    this.setState({modalOpen: !this.state.modalOpen, logout: answer});
  }
  render() {
    const self = this;
    if (this.props.authenticated && this.state.logout) {
      NetworkApi.get('/logout')
           .then(function (response) {
             self.props.history.push("/");
             self.props.changeAuthState(false);
           })
          .catch(function (response) {
            self.props.changeAuthState(true);
          });
    }
    else if (!this.state.modalOpen && !this.state.logout) {
      self.props.history.goBack();
    }
    return (
      <SimpleConfirmationModal
        modalOpen={this.state.modalOpen}
        reply={this.toggleModal}
        title="Logout"
        question="Are you sure you want to log out"
        header="warningModalHeader"
      />
    );
  }
}
Logout.PropTypes = {
  authenticated: PropTypes.bool.isRequired,
  changeAuthState: PropTypes.func.isRequired,
}
Logout.defaultProps = {}
export default Logout;
