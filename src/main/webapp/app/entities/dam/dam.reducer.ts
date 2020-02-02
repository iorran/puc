import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDam, defaultValue } from 'app/shared/model/dam.model';

export const ACTION_TYPES = {
  FETCH_DAM_LIST: 'dam/FETCH_DAM_LIST',
  FETCH_DAM: 'dam/FETCH_DAM',
  CREATE_DAM: 'dam/CREATE_DAM',
  UPDATE_DAM: 'dam/UPDATE_DAM',
  DELETE_DAM: 'dam/DELETE_DAM',
  RESET: 'dam/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDam>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DamState = Readonly<typeof initialState>;

// Reducer

export default (state: DamState = initialState, action): DamState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DAM):
    case REQUEST(ACTION_TYPES.UPDATE_DAM):
    case REQUEST(ACTION_TYPES.DELETE_DAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DAM):
    case FAILURE(ACTION_TYPES.CREATE_DAM):
    case FAILURE(ACTION_TYPES.UPDATE_DAM):
    case FAILURE(ACTION_TYPES.DELETE_DAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_DAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DAM):
    case SUCCESS(ACTION_TYPES.UPDATE_DAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/dams';

// Actions

export const getEntities: ICrudGetAllAction<IDam> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DAM_LIST,
    payload: axios.get<IDam>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDam> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DAM,
    payload: axios.get<IDam>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDam> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DAM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
