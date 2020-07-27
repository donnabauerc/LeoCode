import * as rm from 'typed-rest-client/RestClient';

export async function runRequest() {
    let rest: rm.RestClient = new rm.RestClient('rest-samples', 'http://localhost:8080');

    let res: rm.IRestResponse<String> = await rest.get<String>('/hello');
    console.log(res.statusCode);
    console.log(res.result);
}