export interface IFrontendNachrichtEvent {
    typ: 'ANZEIGE';
    id: number;
    art: 'CREATE' | 'UPDATE' | 'DELETE';
}