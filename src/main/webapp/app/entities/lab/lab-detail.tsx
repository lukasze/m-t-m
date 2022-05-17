import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lab.reducer';

export const LabDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const labEntity = useAppSelector(state => state.lab.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="labDetailsHeading">Lab</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{labEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{labEntity.title}</dd>
          <dt>
            <span id="descritpion">Descritpion</span>
          </dt>
          <dd>{labEntity.descritpion}</dd>
          <dt>Student</dt>
          <dd>
            {labEntity.students
              ? labEntity.students.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {labEntity.students && i === labEntity.students.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/lab" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lab/${labEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LabDetail;
