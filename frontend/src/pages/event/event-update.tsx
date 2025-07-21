import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Form,
  Input,
  DatePicker,
  InputNumber,
  Select,
  Button,
  Spin,
} from 'antd';
import dayjs from 'dayjs';
import { EventStatus } from '../../shared/types/enumerations/event-enum';
import { useAppDispatch, useAppSelector } from '../../services/config/redux/hooks';
import type { IEvent } from '../../shared/types/event-model';
import { fetchEventByIdThunk, updateEventThunk } from './event-slice';

const { Option } = Select;

const EventUpdate: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const { currentEvent, loading } = useAppSelector((state) => state.event);

  useEffect(() => {
    if (id) dispatch(fetchEventByIdThunk(Number(id)));
  }, [dispatch, id]);

  useEffect(() => {
    if (currentEvent) {
      form.setFieldsValue({
        title: currentEvent.title,
        price: parseFloat(currentEvent.price),
        status: currentEvent.status,
        dateRange: [dayjs(currentEvent.startDate), dayjs(currentEvent.endDate)],
      });
    }
  }, [currentEvent, form]);

  const handleSubmit = (values: any) => {
    const payload: IEvent = {
      id: Number(id),
      title: values.title,
      price: values.price.toString(),
      status: values.status,
      startDate: values.dateRange[0].toISOString(),
      endDate: values.dateRange[1].toISOString(),
    };
    
    dispatch(updateEventThunk(payload)).then((action) => {
      if (updateEventThunk.fulfilled.match(action)) {
        navigate('/events');
      }
    });
  };

  if (loading || !currentEvent) return <Spin fullscreen />;

  return (
    <div style={{ padding: 24, maxWidth: 600, margin: '0 auto' }}>
      <h2>Edit Event</h2>
      <Form layout="vertical" form={form} onFinish={handleSubmit}>
        <Form.Item name="title" label="Title" rules={[{ required: true }]}>
          <Input />
        </Form.Item>

        <Form.Item name="price" label="Price (R$)" rules={[{ required: true }]}>
          <InputNumber style={{ width: '100%' }} min={0} />
        </Form.Item>

        <Form.Item name="status" label="Status" rules={[{ required: true }]}>
          <Select>
            {Object.values(EventStatus).map(status => (
              <Option key={status} value={status}>
                {status}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          name="dateRange"
          label="Date Range"
          rules={[{ required: true, message: 'Please select start and end dates' }]}
        >
          <DatePicker.RangePicker
            showTime
            format="DD/MM/YYYY HH:mm"
            style={{ width: '100%' }}
          />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" block>
            Save
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default EventUpdate;
