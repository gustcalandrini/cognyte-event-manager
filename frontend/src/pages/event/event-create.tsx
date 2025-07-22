// src/pages/event/event-create.tsx

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, DatePicker, InputNumber, Select, Button } from 'antd';
import { EventStatus } from '../../shared/types/enumerations/event-enum';
import type { IEvent } from '../../shared/types/event-model';
import { useAppDispatch } from '../../services/config/redux/hooks';
import { createEventThunk } from './event-slice';

const { Option } = Select;

const EventCreate: React.FC = () => {
    const [form] = Form.useForm();
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const handleSubmit = async (values: any) => {
        const payload: IEvent = {
            title: values.title,
            price: values.price.toString(),
            status: values.status,
            startDate: values.dateRange[0].toISOString(),
            endDate: values.dateRange[1].toISOString(),
        };

        dispatch(createEventThunk(payload)).then((action) => {
            if (createEventThunk.fulfilled.match(action)) {
                navigate('/events');
            }
        });
    };

    return (
        <div style={{ padding: 24, maxWidth: 600, margin: '0 auto' }}>
            <h2>Create Event</h2>
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
                    <DatePicker.RangePicker showTime format="DD/MM/YYYY HH:mm" style={{ width: '100%' }} />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" block>
                        Create
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default EventCreate;
