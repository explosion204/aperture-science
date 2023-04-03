import PropTypes from 'prop-types';
import Button from './Button';
import { FaTimes } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { useContext } from 'react';
import { useItemContext } from '../contexts/ItemContext';

const Item = ({ item }) => {
    const { onItemUpdate, onItemDelete } = useItemContext();
    const markItem = () => {
        item.marked = !item.marked;
        onItemUpdate(item);
    };

    return (
        <div className='card w-100 m-2' style={{ backgroundColor: 'whitesmoke' }}>
            <div className='card-body'>
                <div className='d-flex'>
                    <h5 className='card-title'>{item.title}</h5>
                    <FaTimes className='ms-auto' 
                             style={{ cursor: 'pointer', color: 'red' }}
                             onClick={() => onItemDelete(item.id)}/>
                </div>
                <p className='card-text'>{item.description}</p>

                <Button text={item.marked ? 'Unmark' : 'Mark'} 
                    color={item.marked ? 'green' : 'gray'} 
                    btnClass='ms-auto'
                    onClick={markItem}/>
                <Link to={`/item/${item.id}`}>
                    <Button text='View'
                            color='gray'/>
                </Link>
            </div>
        </div>
    );
};

Item.propTypes = {
    items: PropTypes.object
};

export default Item;