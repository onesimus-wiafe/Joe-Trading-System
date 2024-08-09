import { Injectable, OnInit, signal } from '@angular/core';
import { Client } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService implements OnInit {
  constructor() {}

  ngOnInit(): void {
    console.log('Setting up client');
    this.client.set(this.setupClient());
  }

  client = signal<Client | null>(null);
  connected = signal(false);

  setupClient() {
    console.log('Setting up client');
    const client = new Client({
      brokerURL: 'ws://localhost:5002/api/v1/report/ws',
    });

    client.onConnect = (frame) => {
      this.connected.set(true);
      console.log('Connected: ' + client.connected + ' : ' + frame);
      client.subscribe('/topic/greetings', (greeting) => {
        console.log(greeting);
      });
    };

    client.onWebSocketError = (error) => {
      console.error('Error with websocket', error);
    };

    client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    return client;
  }

  connect() {
    console.log('Connecting');
    this.client()?.activate();
  }

  disconnect() {
    this.client()?.deactivate();
    this.connected.set(false);
    console.log('Disconnected');
  }
}
