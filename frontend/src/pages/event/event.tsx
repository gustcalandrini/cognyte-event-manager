import React, { useEffect } from 'react';
import {
  Card,
  Col,
  Row,
  Typography,
  Spin,
  message,
  Tag,
  Pagination,
  Popconfirm,
} from 'antd';
import { EditOutlined, DeleteOutlined } from '@ant-design/icons';
import {
  fetchEventsThunk,
  deleteEventThunk,
} from './event-slice';

import dayjs from 'dayjs';
import { useAppDispatch, useAppSelector } from '../../services/config/redux/hooks';
import type { AppDispatch, RootState } from '../../services/config/redux/store';
import { EventStatus } from '../../shared/types/enumerations/event-enum';
import type { IEvent } from '../../shared/types/event-model';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const pageSize = 10;

const EventListPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch<AppDispatch>();
  const { events, loading, current, total } = useAppSelector((state: RootState) => state.event);

  const loadEvents = (page: number) => {
    dispatch(fetchEventsThunk({ page, pageSize }));
  };

  useEffect(() => {
    loadEvents(current);
  }, [dispatch]);

  const handleDelete = async (id: number) => {
    dispatch(deleteEventThunk(id)).then((action) => {
      if (deleteEventThunk.fulfilled.match(action)) {
        loadEvents(current);
      }
    });
  };

  const handleEdit = (event: IEvent) => {
    navigate(`/events/${event.id}/edit`);
  };

  const getStatusColor = (status: EventStatus) => {
    switch (status) {
      case EventStatus.COMPLETED:
        return 'green';
      case EventStatus.PAUSED:
        return 'red';
      case EventStatus.STARTED:
        return 'blue';
      default:
        return 'gray';
    }
  };

  if (loading) {
    return <Spin fullscreen />;
  }

  console.log('Events:', events);

  return (
    <div style={{ padding: '24px' }}>
      <Title level={2}>Event List</Title>

      <Row gutter={[16, 16]}>
        {events.map((event) => (
          <Col xs={24} sm={12} md={8} lg={6} key={event.id}>
            <Card
              title={event.title}
              variant='outlined'
              extra={<Tag color={getStatusColor(event.status)}>{event.status}</Tag>}
              actions={[
                <EditOutlined key="edit" onClick={() => handleEdit(event)} />,
                <Popconfirm
                  key="delete"
                  title="Are you sure you want to delete this event?"
                  onConfirm={() => handleDelete(event.id)}
                  okText="Yes"
                  cancelText="No"
                >
                  <DeleteOutlined />
                </Popconfirm>,
              ]}
            >
              <p>
                <Text strong>Start:</Text> {dayjs(event.startDate).format('DD/MM/YYYY HH:mm')}
              </p>
              <p>
                <Text strong>End:</Text> {dayjs(event.endDate).format('DD/MM/YYYY HH:mm')}
              </p>
              <p>
                <Text strong>Price:</Text> R$ {Number(event.price).toFixed(2)}
              </p>
            </Card>
          </Col>
        ))}
      </Row>

      <div style={{ marginTop: 24, textAlign: 'center' }}>
        <Pagination
          current={current}
          total={total}
          pageSize={pageSize}
          onChange={(page) => loadEvents(page)}
        />
      </div>
    </div>
  );
};

export default EventListPage;
