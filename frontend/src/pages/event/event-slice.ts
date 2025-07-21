import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type { IEvent } from '../../shared/types/event-model';
import api from '../../services/config/axios/api';

interface EventState {
    currentEvent: IEvent | null;
    events: IEvent[];
    total: number;
    current: number;
    loading: boolean;
    error: string | null;
}

const initialState: EventState = {
    currentEvent: null,
    events: [],
    total: 0,
    current: 1,
    loading: false,
    error: null,
};

export const fetchEventsThunk = createAsyncThunk(
    'event/fetchEvents',
    async (
        { page, pageSize }: { page: number; pageSize: number },
        thunkAPI
    ) => {
        try {
            const res = await api.get<{ data: IEvent[]; total: number; current: number }>(
                `/events?page=${page}&pageSize=${pageSize}`
            );
            return res.data;
        } catch (error) {
            return thunkAPI.rejectWithValue(
                (error as any)?.response?.data?.message || (error as any)?.message || 'Failed to fetch events'
            );
        }
    }
);

export const fetchEventByIdThunk = createAsyncThunk(
    'event/fetchById',
    async (id: number, thunkAPI) => {
        try {
            const res = await api.get<IEvent>(`/events/${id}`);
            return res.data;
        } catch (error) {
            return thunkAPI.rejectWithValue(
                (error as any)?.response?.data?.message || (error as any)?.message || 'Failed to fetch event'
            );
        }
    }
);

export const updateEventThunk = createAsyncThunk(
    'event/update',
    async (eventData: IEvent, thunkAPI) => {
        try {
            const res = await api.put<IEvent>(`/events/${eventData.id}`, eventData, {
                headers: {
                    'X-Success-Message': 'Event updated successfully',
                },
            });
            return res.data;
        } catch (error) {
            return thunkAPI.rejectWithValue(
                (error as any)?.response?.data?.message || (error as any)?.message || 'Failed to update event'
            );
        }
    }
);

export const createEventThunk = createAsyncThunk(
    'event/create',
    async (eventData: IEvent, thunkAPI) => {
        try {
            const res = await api.post<IEvent>(`/events`, eventData, {
                headers: {
                    'X-Success-Message': 'Event created successfully',
                },
            });
            return res.data;
        } catch (error) {
            return thunkAPI.rejectWithValue(
                (error as any)?.response?.data?.message || (error as any)?.message || 'Failed to create event'
            );
        }
    }
);

export const deleteEventThunk = createAsyncThunk(
    'event/deleteEvent',
    async (eventId: number, thunkAPI) => {
        try {
            await api.delete(`/events/${eventId}`, {
                headers: {
                    'X-Success-Message': 'Event deleted successfully',
                },
            });
            return eventId;
        } catch (error) {
            return thunkAPI.rejectWithValue(
                (error as any)?.response?.data?.message || (error as any)?.message || 'Failed to delete event'
            );
        }
    }
);


const eventSlice = createSlice({
    name: 'event',
    initialState,
    reducers: {
        clearCurrentEvent(state) {
            state.currentEvent = null;
        },
    },
    extraReducers: (builder) => {
        builder
            // Fetch paginated list
            .addCase(fetchEventsThunk.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchEventsThunk.fulfilled, (state, action) => {
                state.events = action.payload.data;
                state.total = action.payload.total;
                state.current = action.payload.current;
                state.loading = false;
            })
            .addCase(fetchEventsThunk.rejected, (state, action) => {
                state.loading = false;
                state.error = (action.payload as any)?.message || 'Error loading events';
            })
            .addCase(fetchEventByIdThunk.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.currentEvent = null;
            })
            .addCase(fetchEventByIdThunk.fulfilled, (state, action: PayloadAction<IEvent>) => {
                state.currentEvent = action.payload;
                state.loading = false;
            })
            .addCase(fetchEventByIdThunk.rejected, (state, action) => {
                state.loading = false;
                state.error = (action.payload as any)?.message || 'Error loading event';
            })
            .addCase(updateEventThunk.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateEventThunk.fulfilled, (state, action) => {
                state.loading = false;
            })
            .addCase(updateEventThunk.rejected, (state, action) => {
                state.loading = false;
                state.error = (action.payload as any)?.message || 'Failed to update event';
            })

            // Create event
            .addCase(createEventThunk.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(createEventThunk.fulfilled, (state, action: PayloadAction<IEvent>) => {
                state.loading = false;
                state.events.push(action.payload);
            })
            .addCase(createEventThunk.rejected, (state, action) => {
                state.loading = false;
                state.error = (action.payload as any)?.message || 'Failed to create event';
            })

            // Delete event
            .addCase(deleteEventThunk.fulfilled, (state, action: PayloadAction<number>) => {
                state.events = state.events.filter(event => event.id !== action.payload);
            });
    },
});

export default eventSlice.reducer;