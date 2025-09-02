// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /allive/hello */
export async function hello(options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/allive/hello', {
    method: 'GET',
    ...(options || {}),
  })
}
