import{R as _,ax as B,A as m,z as R,d as A,D as p,ao as E,cA as H,F as L,E as N,H as k,a as O,aL as V,aM as b}from"./index-D_-DzWhp.js";let y=!1;function j(){if(_&&window.CSS&&!y&&(y=!0,"registerProperty"in(window==null?void 0:window.CSS)))try{CSS.registerProperty({name:"--n-color-start",syntax:"<color>",inherits:!1,initialValue:"#0000"}),CSS.registerProperty({name:"--n-color-end",syntax:"<color>",inherits:!1,initialValue:"#0000"})}catch{}}function F(e){const{heightSmall:o,heightMedium:i,heightLarge:r,borderRadius:n}=e;return{color:"#eee",colorEnd:"#ddd",borderRadius:n,heightSmall:o,heightMedium:i,heightLarge:r}}const M={common:B,self:F},T=m([R("skeleton",`
 height: 1em;
 width: 100%;
 transition:
 --n-color-start .3s var(--n-bezier),
 --n-color-end .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 animation: 2s skeleton-loading infinite cubic-bezier(0.36, 0, 0.64, 1);
 background-color: var(--n-color-start);
 `),m("@keyframes skeleton-loading",`
 0% {
 background: var(--n-color-start);
 }
 40% {
 background: var(--n-color-end);
 }
 80% {
 background: var(--n-color-start);
 }
 100% {
 background: var(--n-color-start);
 }
 `)]),$=Object.assign(Object.assign({},k.props),{text:Boolean,round:Boolean,circle:Boolean,height:[String,Number],width:[String,Number],size:String,repeat:{type:Number,default:1},animated:{type:Boolean,default:!0},sharp:{type:Boolean,default:!0}}),I=A({name:"Skeleton",inheritAttrs:!1,props:$,setup(e){j();const{mergedClsPrefixRef:o}=N(e),i=k("Skeleton","-skeleton",T,M,e,o);return{mergedClsPrefix:o,style:O(()=>{var r,n;const a=i.value,{common:{cubicBezierEaseInOut:S}}=a,g=a.self,{color:v,colorEnd:w,borderRadius:x}=g;let s;const{circle:l,sharp:z,round:C,width:t,height:d,size:f,text:h,animated:P}=e;f!==void 0&&(s=g[V("height",f)]);const c=l?(r=t??d)!==null&&r!==void 0?r:s:t,u=(n=l?t??d:d)!==null&&n!==void 0?n:s;return{display:h?"inline-block":"",verticalAlign:h?"-0.125em":"",borderRadius:l?"50%":C?"4096px":z?"":x,width:typeof c=="number"?b(c):c,height:typeof u=="number"?b(u):u,animation:P?"":"none","--n-bezier":S,"--n-color-start":v,"--n-color-end":w}})}},render(){const{repeat:e,style:o,mergedClsPrefix:i,$attrs:r}=this,n=p("div",E({class:`${i}-skeleton`,style:o},r));return e>1?p(L,null,H(e,null).map(a=>[n,`
`])):n}});export{I as _};
