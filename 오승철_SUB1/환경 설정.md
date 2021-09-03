## 1. 개발 환경 구성



#### 1) 아나콘다 설치

https://www.anaconda.com/products/individual 에 접속해서 Windows용 아나콘다 설치

- 설치 화면에서 환경 변수를 설정하겠다라는 체크 박스를 하는 것을 추천!
  - 하지 않을 경우 개별적으로 추가해주어야 함
- 콘다의 Python 3.8을 기본 Path로 설정하는 체크 박스는 필요 없음
  - 현재 이미 로컬에 Python이 있으므로



#### 2) 가상환경 생성 및 활성화

```
conda create -n py37 python=3.7
```

위 명령어를 통해 가상환경을 생성한다.



하지만 내 로컬에서는 다음과 같은 프롬프트 에러가 발생

```
Collecting package metadata (current_repodata.json): failed

CondaHTTPError: HTTP 000 CONNECTION FAILED for url <https://repo.anaconda.com/pkgs/main/win-64/current_repodata.json>
Elapsed: -

An HTTP error occurred when trying to retrieve this URL.
HTTP errors are often intermittent, and a simple retry will get you on your way.

If your current network has https://www.anaconda.com blocked, please file
a support request with your network engineering team.

'https://repo.anaconda.com/pkgs/main/win-64'
```

url을 호출하는 상황에 있어 연결이 끊겼다고 한다. 해당 url을 직접 입력하고 들어가면 json 형태로 정상 확인이 된다.



구글링을 해보니 ssl 인증서 관련 이슈이다. 2가지 해결 방법을 제시하고 있다.

- conda에서 ssl을 사용하지 않는 것
- ssl 인증서를 설치해서 인증을 받는 것



후자의 방법은 귀찮은 작업들의 연속이라 ssl을 사용하지 않는 방법으로 진행하겠다.

```
conda config --set ssl_verify no
```



해결 되지 않았다. 구글링을 계속 해보니, 내가 직접 연결한 환경변수의 문제가 있는 것 같았고 환경변수를 추가했다.

`[아나콘다 경로]/Library/bin` 

(수정하지 않았다면 아나콘다 경로는 **C:\Users\컴퓨터사용자이름\Anaconda3\\** 일 것이다)

```
done
#
# To activate this environment, use
#
#     $ conda activate py37
#
# To deactivate an active environment, use
#
#     $ conda deactivate
```



가상환경 활성화를 한다.

```
conda activate py37
```



하지만 어림도 없지 그냥 되는법이 없다.

현재 나의 shell에서는 `conda activate` 라는 명령어를 사용할 수 없다고 한다.

사용하고 싶다면 shell을 등록하라고 한다.

```
CommandNotFoundError: Your shell has not been properly configured to use 'conda activate'.
If using 'conda activate' from a batch script, change your
invocation to 'CALL conda.bat activate'.

To initialize your shell, run

    $ conda init <SHELL_NAME>

Currently supported shells are:
  - bash
  - cmd.exe
  - fish
  - tcsh
  - xonsh
  - zsh
  - powershell

See 'conda init --help' for more information and options.

IMPORTANT: You may need to close and restart your shell after running 'conda init'.
```



그래서 나는 현재 `bash` shell을 사용하고 있으므로 bash를 추가해 보겠다.

```
$ conda init bash
no change     C:\Users\tmdcj\anaconda3\Scripts\conda.exe
no change     C:\Users\tmdcj\anaconda3\Scripts\conda-env.exe
no change     C:\Users\tmdcj\anaconda3\Scripts\conda-script.py
no change     C:\Users\tmdcj\anaconda3\Scripts\conda-env-script.py
no change     C:\Users\tmdcj\anaconda3\condabin\conda.bat
no change     C:\Users\tmdcj\anaconda3\Library\bin\conda.bat
no change     C:\Users\tmdcj\anaconda3\condabin\_conda_activate.bat
no change     C:\Users\tmdcj\anaconda3\condabin\rename_tmp.bat
no change     C:\Users\tmdcj\anaconda3\condabin\conda_auto_activate.bat
no change     C:\Users\tmdcj\anaconda3\condabin\conda_hook.bat
no change     C:\Users\tmdcj\anaconda3\Scripts\activate.bat
no change     C:\Users\tmdcj\anaconda3\condabin\activate.bat
no change     C:\Users\tmdcj\anaconda3\condabin\deactivate.bat
modified      C:\Users\tmdcj\anaconda3\Scripts\activate
modified      C:\Users\tmdcj\anaconda3\Scripts\deactivate
modified      C:\Users\tmdcj\anaconda3\etc\profile.d\conda.sh
modified      C:\Users\tmdcj\anaconda3\etc\fish\conf.d\conda.fish
no change     C:\Users\tmdcj\anaconda3\shell\condabin\Conda.psm1
modified      C:\Users\tmdcj\anaconda3\shell\condabin\conda-hook.ps1
no change     C:\Users\tmdcj\anaconda3\Lib\site-packages\xontrib\conda.xsh
modified      C:\Users\tmdcj\anaconda3\etc\profile.d\conda.csh
modified      C:\Users\tmdcj\.bash_profile

==> For changes to take effect, close and re-open your current shell. <==
```

몇몇 파일이 수정됐고 `bash`를 껐다가 키니 이제 정상 작동한다.



```
(py37)
Oh@ ~/
$
```

위와 같이 확인이 되면 가상환경이 켜진 것이다.



~~이전에는 콘다 설정이 이렇게 복잡하지 않았던 것으로 기억하는데..~~



#### 3) 프레임워크 및 라이브러리 설치

파이토치와 텐서플로우를 설치한다.

```
$ conda install pytorch torchvision torchaudio cudatoolkit=10.1 -c pytorch
```

```
$ pip install tensorflow-gpu
```



의존하는 라이브러리와 같이 설치되며 해당 라이브러리들은 내가 설정한 **가상환경 py37 (python 3.7 버전)**에서만 존재하는 걸 잊지말자!