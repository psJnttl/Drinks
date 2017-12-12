import React from 'react';
import PropTypes from 'prop-types';
import {Button} from 'react-bootstrap';

const GitHubButton = (props) => (
      <div>
        <a href="/api/login/github">
          <Button>
            <img src={"/images/GitHub-Mark-32px.png"} alt={"github logo"} />
            <span style={{'marginLeft':'4px'}}>login via GitHub</span>
          </Button>
        </a>
      </div>
);

GitHubButton.PropTypes = {}
GitHubButton.defaultProps = {}
export default GitHubButton;
