$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$dist = Join-Path $root "dist"
$inputDirectory = Join-Path $root "target\jpackage-input"

$resolvedRoot = [IO.Path]::GetFullPath($root)
$resolvedDist = [IO.Path]::GetFullPath($dist)
if (-not $resolvedDist.StartsWith($resolvedRoot + [IO.Path]::DirectorySeparatorChar)) {
    throw "Refusing to clean an output folder outside the repository."
}

mvn -B clean verify
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Remove-Item -LiteralPath $dist -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item -LiteralPath $inputDirectory -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $dist, $inputDirectory -Force | Out-Null
Copy-Item (Join-Path $root "target\aps-patient-data-1.0.0-all.jar") (Join-Path $inputDirectory "app.jar")

$jpackage = Join-Path $env:JAVA_HOME "bin\jpackage.exe"
if (-not (Test-Path -LiteralPath $jpackage)) {
    $jpackage = (Get-Command jpackage -ErrorAction Stop).Source
}

& $jpackage --type app-image --name "APS Patient Data" --app-version "1.0.0" `
    --vendor "Sofoste" --input $inputDirectory --main-jar "app.jar" `
    --main-class "com.sofoste.apspatientdata.Launcher" --dest $dist
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$archive = Join-Path $dist "APS-Patient-Data-Windows-x64.zip"
Compress-Archive -LiteralPath (Join-Path $dist "APS Patient Data") -DestinationPath $archive -Force
Write-Host "Package created: $archive"
