# docker
> 使用脚本安装 Docker
* 1、获取最新版本的 Docker 安装包
```
wget -qO- https://get.docker.com/ | sh
```
* 启动/停止 docker 后台服务
```
sudo service docker start
sudo service docker stop
```
* 建立 docker 用户组  
默认情况下， docker 命令会使用 Unix socket 与 Docker 引擎通讯。而只有
root 用户和 docker 组的用户才可以访问 Docker 引擎的 Unix socket。出于
安全考虑，一般 Linux 系统上不会直接使用 root 用户。因此，更好地做法是将
需要使用 docker 的用户加入 docker 用户组。
建立 docker 组：
```
$ sudo groupadd docker
#将当前用户加入 docker 组：
$ sudo usermod -aG docker $USER
```
> Docker 使用  
* Docker 允许你在容器内运行应用程序， 使用 docker run 命令来在容器内运行一个应用程序。
```
docker run ubuntu:15.10 /bin/echo "Hello world"
# 各个参数解析：
#   docker: Docker 的二进制执行文件。
#   run:与前面的 docker 组合来运行一个容器。
#   ubuntu:15.10指定要运行的镜像，Docker首先从本地主机上查找镜像是否存在，如果不存在，Docker 就会从镜像仓库 Docker Hub 下载公共镜像。
#   /bin/echo "Hello world": 在启动的容器里执行的命令
#可以解释为：Docker 以 ubuntu15.10 镜像创建一个新容器，然后在容器里执行 bin/echo "Hello world"，然后输出结果。
```
> 运行交互式的容器
* 通过docker的两个参数 -i -t(-it)，让docker运行的容器实现"对话"的能力,可以通过运行exit命令或者使用CTRL+D来退出容器。
```
docker run -i -t --rm ubuntu:15.10 /bin/bash
# 各个参数解析：
# -t:在新容器内指定一个伪终端或终端。
# -i:允许你对容器内的标准输入 (STDIN) 进行交互。
# --rm ：这个参数是说容器退出后随之将其删除。默认情况下，为了排障需求，退出的容器并不会立即删除，除非手动 docker rm 。我们这里只是随便执行个命令，
         看看结果，不需要排障和保留结果，因此使用 --rm 可以避免浪费空间。
```

> 获取镜像
* 从 Docker 镜像仓库获取镜像的命令是 docker pull 。其命令格式为：
```
docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签]
```
具体的选项可以通过 docker pull --help 命令看到，这里我们说一下镜像名称
的格式。
Docker 镜像仓库地址：地址的格式一般是 `<域名/IP>[:端口号]` 。默认地址
是 Docker Hub。
仓库名：如之前所说，这里的仓库名是两段式名称，即 <用户名>/<软件名> 。
对于 Docker Hub，如果不给出用户名，则默认为 library ，也就是官方镜
像。

比如：
```
$ docker pull ubuntu:16.04
16.04: Pulling from library/ubuntu
bf5d46315322: Pull complete
9f13e0ac480c: Pull complete
e8988b5b3097: Pull complete
40af181810e7: Pull complete
e6f7c7e5c03e: Pull complete
Digest: sha256:147913621d9cdea08853f6ba9116c2e27a3ceffecf3b49298
3ae97c3d643fbbe
Status: Downloaded newer image for ubuntu:16.04
```
上面的命令中没有给出 Docker 镜像仓库地址，因此将会从 Docker Hub 获取镜
像。而镜像名称是 ubuntu:16.04 ，因此将会获取官方镜像 `library/ubuntu`
仓库中标签为 16.04 的镜像。从下载过程中可以看到我们之前提及的分层存储的概念，镜像是由多层存储所构
成。下载也是一层层的去下载，并非单一文件。下载过程中给出了每一层的 ID 的
前 12 位。并且下载结束后，给出该镜像完整的 sha256 的摘要，以确保下载一致性。

> 列出镜像
* 要想列出已经下载下来的镜像，可以使用 docker image ls 命令。
```
root@VM-141525ec-0248-4754-b6cd-40696e1a576d:~# docker image ls
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE

#列表包含了 仓库名 、 标签 、 镜像 ID 、 创建时间 以及 所占用的空间 。
```
* 你可以通过以下命令来便捷的查看镜像、容器、数据卷所占用的空间。
```
root@VM-141525ec-0248-4754-b6cd-40696e1a576d:~#  docker system df

TYPE                TOTAL               ACTIVE              SIZE                RECLAIMABLE
Images              2                   2                   137.2MB             0B (0%)
Containers          6                   0                   54B                 54B (100%)
Local Volumes       0                   0                   0B                  0B
Build Cache         0                   0                   0B                  0B
```
> 删除本地镜像  
  如果要删除本地的镜像，可以使用 docker image rm 命令，其格式为：
   ```
   $ docker image rm [选项] <镜像1> [<镜像2> ...]
   ```
> 镜像是多层存储，每一层是在前一层的基础上进行修改；而容器同样也是多层存储，是在以镜像为基础层，在其基础上加一层作为容器运行时的存储层。  
> 定制一个 Web 服务器为例子，来讲解镜像是如何构建的。  
    ```
    $ docker run --name webserver -d -p 80:80 nginx
    ```  
    这条命令会用 nginx 镜像启动一个容器，-d: 后台运行,命名为 webserver ，并且映射了 80 端口，这样我们可以用浏览器去访问这个 nginx 服务器。
    
>* 我们可以了解到，镜像的定制实际上就是
  定制每一层所添加的配置、文件。如果我们可以把每一层修改、安装、构建、操作
  的命令都写入一个脚本，用这个脚本来构建、定制镜像，那么之前提及的无法重复
  的问题、镜像构建透明性的问题、体积的问题就都会解决。这个脚本就是Dockerfile。  
>* 使用 Dockerfile 来定制。  
    ```
    $ mkdir mynginx
    $ cd mynginx
    $ touch Dockerfile
    ```  
    其内容为：  
    ```
    FROM nginx RUN echo '<h1>Hello, Docker!</h1>' > /usr/share/nginx/html/index.html
    ```  
    这个 Dockerfile 很简单，一共就两行。涉及到了两条指令， FROM 和 RUN 。  
    *FROM 指定基础镜像* 所谓定制镜像，那一定是以一个镜像为基础，在其上进行定制。就像我们之前运行了一个 nginx 镜像的容器，再进行修改一样，基础镜像是必须指定的。而
     FROM 就是指定基础镜像，因此一个 Dockerfile 中 FROM 是必备的指令，并且必须是第一条指令。除了选择现有镜像为基础镜像外，Docker 还存在一个特殊的镜像，名为
     scratch 。这个镜像是虚拟的概念，并不实际存在，它表示一个空白的镜像。  
     *RUN 执行命令* RUN 指令是用来执行命令行命令的。由于命令行的强大能力， RUN 指令在定制镜像时是最常用的指令之一。其格式有两种：  
     shell 格式： RUN <命令> ，就像直接在命令行中输入的命令一样。刚才写的Dockerfile 中的 RUN 指令就是这种格式。  
     exec 格式： RUN `["可执行文件", "参数1", "参数2"]` ，这更像是函数调用中的格式。
     
> 构建镜像  
在 Dockerfile 文件所在目录执行：  
    ```
    root@VM-141525ec-0248-4754-b6cd-40696e1a576d:/home/mynginx# docker build -t nginx:v3 .
    Sending build context to Docker daemon  2.048kB
    Step 1/2 : FROM nginx
     ---> e81eb098537d
    Step 2/2 : RUN echo '<h1>Hello, Docker!</h1>' > /usr/share/nginx/html/index.html
     ---> Running in 8c7cb21c5bd5
    Removing intermediate container 8c7cb21c5bd5
     ---> 644d3faddf51
    Successfully built 644d3faddf51
    Successfully tagged nginx:v3
    root@VM-141525ec-0248-4754-b6cd-40696e1a576d:/home/mynginx#
    ```    
    从命令的输出结果中，我们可以清晰的看到镜像的构建过程。在 Step 2 中，如同我们之前所说的那样， RUN 指令启动了一个容器 8c7cb21c5bd5 ，执行了所
    要求的命令，并最后提交了这一层 644d3faddf51 ，随后删除了所用到的这个容器 8c7cb21c5bd5 。  
    这里我们使用了 docker build 命令进行镜像构建。其格式为：  
    ```
    docker build [选项] <上下文路径/URL/->
    ```  
    在这里我们指定了最终镜像的名称 -t nginx:v3 ，构建成功后，我们可以使用`docker run --name web2 -d -p 81:80 nginx:v3`来运行这个镜像。  
    如果目录下有些东西确实不希望构建时传给 Docker 引擎，那么可以用 .gitignore 一样的语法写一个 .dockerignore ，该文件是用于剔除不需要作为上下文传递给 Docker 引擎的。

>其它 docker build 的用法  
>* 其它 docker build 的用法, docker build 还支持从 URL 构建，比如可以直接从 Git repo 中构建：  
>* 用给定的 tar 压缩包构建
>* 从标准输入中读取 Dockerfile 进行构建 `docker build - < Dockerfile`或`cat Dockerfile | docker build -`

> 启动容器  
启动容器有两种方式，一种是基于镜像新建一个容器并启动，另外一个是将在终止状态（ stopped ）的容器重新启动。
因为 Docker 的容器实在太轻量级了，很多时候用户都是随时删除和新创建容器。  
启动已终止容器：可以利用 docker container start 命令，直接将一个已经终止的容器启动运行。  
注： 容器是否会长久运行，是和 docker run 指定的命令有关，和 -d 参数无关。
使用 -d 参数启动后会返回一个唯一的 id，也可以通过 docker container ls 命令来查看容器信息。  
要获取容器的输出信息，可以通过 docker container logs 命令。

>终止容器  
可以使用 docker container stop 来终止一个运行中的容器。终止状态的容器可以用 docker container ls -a 命令看到。

>删除容器  
可以使用 docker container rm 来删除一个处于终止状态的容器。  
清理所有处于终止状态的容器:docker container prune


docker run --name web3 -d -p 80:80 -v  $PWD/nginx.conf:/etc/nginx/nginx.conf nginx:v3

docker run --name webserver -d -p 80:80 nginx

docker run -p 81:81 --name mynginx -v $PWD/www:/www -v $PWD/conf/nginx.conf:/etc/nginx/nginx.conf -v $PWD/logs:/wwwlogs  -d nginx

docker run -i -t --rm nginx /bin/bash
$ docker stop $(docker ps -a -q) && docker  rm $(docker ps -a -q) //   remove删除所有容器


docker run --network my-net  -p 81:81 --name mynginx -v $PWD/www:/www -v $PWD/conf/nginx.conf:/etc/nginx/nginx.conf -v $PWD/logs:/wwwlogs  -d nginx
                                                                                                                                  
docker run --name web3 -d -p 82:82 -v  $PWD/nginx.conf:/etc/nginx/nginx.conf nginx --network my-net busybox sh

docker run -it --network my-net busybox sh  --name web3 -d -p 82:82 -v  $PWD/nginx.conf:/etc/nginx/nginx.conf nginx 
                                                                       
       docker run -i -t --network my-net --rm nginx /bin/bash                                                                
                                                                       
    bY2*rerpkm  
                                                                         
   mvn clean package docker:build -Dmaven.test.skip=true
   docker run -p 3306:3306 --name mymysql  -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
   
   docker run --name mymysql  -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
   
   docker run -p 81:81 --name springboot  --link mymysql:db  -d springboot/demo 
   
   docker network create -d bridge my-net
   
   
   docker run -p 3306:3306 --name db --network my-net -v $PWD/logs:/logs -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7  
   
   docker run -p 81:81 --name springboot1 --network my-net  -d springboot/demo  
   
   docker run -p 6379:6379 -v $PWD/data:/data --name redis_ -d redis redis-server --appendonly yes
   
   9d78efe6f918e221144e8ee3f9e8ee1d       wx9ab7d11fc11597e5
   
   
   docker run -p 9999:9999 --name springboot  --link mymysql:db  -d springboot/demo 
   
   
   如果深究其日志位置，每个容器的日志默认都会以 json-file 的格式存储于 /var/lib/docker/containers/<容器id>/<容器id>-json.log 下，不过并不建议去这里直接读取内容，因为 Docker 提供了更完善地日志收集方式 - Docker 日志收集驱动。