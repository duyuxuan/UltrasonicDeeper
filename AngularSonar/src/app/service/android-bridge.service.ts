import {Injectable} from '@angular/core';
import {JavaScriptInterface} from '../JavaInterface/JavaScriptInterface';

declare var TrackingService: JavaScriptInterface;

@Injectable({
    providedIn: 'root'
})
export class AndroidBridgeService {
    private readonly TrackingService: JavaScriptInterface;

    constructor() {
        if (typeof TrackingService === 'undefined') {
            console.log('TrackingService is undefined');
            this.TrackingService = new class implements JavaScriptInterface {
                getMapCacheDir(): string {
                    return '';
                }
                downloadMap(map: string): void {
                }

                getActivity(): string {
                    return 'mock';
                }

                saveTrackingList(data: string): void {
                }
            }();
        } else {
            this.TrackingService = TrackingService;
        }
    }

    getActivity(): string {
        return this.TrackingService.getActivity();
    }

    isAvailable() {
        return typeof this.TrackingService !== 'undefined';
    }

    saveTrackingList(data: string) {
        this.TrackingService.saveTrackingList(data);
    }

    getMapCacheDir(): string {
        return this.TrackingService.getMapCacheDir()
    }

    // downloadMap(tiles: MapCoordinates) {
    //     this.TrackingService.downloadMap(JSON.stringify(tiles));
    // }


}

