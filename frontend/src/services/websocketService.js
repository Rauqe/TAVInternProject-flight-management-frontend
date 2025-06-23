import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;
let connected = false;

export function connectFlightWebSocket(onFlightEvent) {
  if (connected) {
    console.log('[WebSocket] Already connected');
    return;
  }
  console.log('[WebSocket] Connecting to ws...');
  const socket = new SockJS('http://localhost:8080/ws');
  stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    debug: (str) => console.log('[WebSocket][DEBUG]', str),
    onConnect: () => {
      connected = true;
      console.log('[WebSocket] Connected!');
      stompClient.subscribe('/topic/flights', (message) => {
        console.log('[WebSocket] Message received:', message.body);
        if (onFlightEvent) {
          onFlightEvent(JSON.parse(message.body));
        }
      });
    },
    onStompError: (frame) => {
      console.error('[WebSocket] STOMP error:', frame);
    },
    onWebSocketError: (event) => {
      console.error('[WebSocket] WebSocket error:', event);
    },
    onDisconnect: () => {
      connected = false;
      console.log('[WebSocket] Disconnected!');
    }
  });
  stompClient.activate();
}

export function disconnectFlightWebSocket() {
  if (stompClient && connected) {
    stompClient.deactivate();
    connected = false;
    console.log('[WebSocket] Disconnected by user');
  }
}