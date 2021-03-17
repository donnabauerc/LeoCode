import {LeoCodeFile} from './leocodefile.model';

export interface Example{
  id: number;
  name: string;
  description: string;
  author: string;
  type: any;
  whitelist: string[];
  blacklist: string[];
  files: LeoCodeFile[];
}
