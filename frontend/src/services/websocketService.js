import { Client } from '@stomp/stompjs';

let stompClient = null;
let connected = false;

export function connectFlightWebSocket(onFlightEvent) {
  if (connected) {
    console.log('[STOMP] Already connected');
    return;
  }
  console.log('[STOMP] Connecting to ws...');
  stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws', // native WebSocket
    reconnectDelay: 5000,
    debug: (str) => console.log('[STOMP][DEBUG]', str),
    onConnect: () => {
      connected = true;
      console.log('[STOMP] Connected!');
      stompClient.subscribe('/topic/flights', (message) => {
        console.log('[STOMP] Message received:', message.body);
        if (onFlightEvent) {
          try {
            onFlightEvent(JSON.parse(message.body));
          } catch (e) {
            console.error('STOMP JSON parse error:', e);
          }
        }
      });
      // Örnek publish (test için):
      // stompClient.publish({ destination: '/app/hello', body: 'Yigit' });
    },
    onStompError: (frame) => {
      console.error('[STOMP] STOMP error:', frame);
    },
    onWebSocketError: (event) => {
      console.error('[STOMP] WebSocket error:', event);
    },
    onDisconnect: () => {
      connected = false;
      console.log('[STOMP] Disconnected!');
    }
  });
  stompClient.activate();
}

export function disconnectFlightWebSocket() {
  if (stompClient && connected) {
    stompClient.deactivate();
    connected = false;
    console.log('[STOMP] Disconnected by user');
  }
}
