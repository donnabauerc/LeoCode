"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.runRequest = void 0;
const rm = require("typed-rest-client/RestClient");
function runRequest() {
    return __awaiter(this, void 0, void 0, function* () {
        let rest = new rm.RestClient('rest-samples', 'http://localhost:8080');
        let res = yield rest.get('/hello');
        console.log(res.statusCode);
        console.log(res.result);
    });
}
exports.runRequest = runRequest;
//# sourceMappingURL=control.js.map